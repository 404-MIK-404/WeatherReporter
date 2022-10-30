package com.telegrambot.BotWeather.service.handler;



import com.telegrambot.BotWeather.model.FavoritesCity;
import com.telegrambot.BotWeather.model.User;
import com.telegrambot.BotWeather.repository.UserRepository;
import com.telegrambot.BotWeather.service.WeatherService;
import com.telegrambot.BotWeather.service.keyboard.InlineKeyboardGenerate;
import com.telegrambot.BotWeather.service.keyboard.ReplyKeyboardGenerate;
import com.telegrambot.BotWeather.enums.BotCommands;
import com.telegrambot.BotWeather.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CommandHandle {


    @Autowired
    private ReplyKeyboardGenerate replyKeyboardGenerate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private InlineKeyboardGenerate inlineKeyboardGenerate;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private UserRepository userRepository;

    @Setter
    private com.telegrambot.BotWeather.model.User user;

    private List<SendMessage> resultMessages = new ArrayList<>();

    @Getter
    @Setter
    private Status botStatus = Status.NOTHING;


    public List<SendMessage> statusNowBot(long chatId,String text) throws TelegramApiException {
        resultMessages.clear();
        SendMessage result = new SendMessage();
        replyKeyboardGenerate.setBotReplyKeyboard(result);
        switch (botStatus){

            case NOTHING:
                сommandExecutionBot(chatId, text,result);
                resultMessages.add(result);
                return resultMessages;

            case ASK_CITY:
                botStatus = Status.NOTHING;
                setMessageProperty(result,chatId, weatherService.weatherCity(text,inlineKeyboardGenerate,result));
                inlineKeyboardGenerate.setInlineKeyboardForecastWeatherCity(result);
                resultMessages.add(result);
                return resultMessages;

            case CREATE_LOGIN:
                botStatus = Status.CREATE_PASSWORD;
                user.setLogin(text);
                setMessageProperty(result,chatId, "Пароль: ");
                resultMessages.add(result);
                return resultMessages;

            case CREATE_PASSWORD:
                botStatus = Status.NOTHING;
                user.setPassword(text);
                registerUser(result,chatId);
                resultMessages.add(result);
                return resultMessages;

            case ENTER_LOGIN:
                botStatus = Status.ENTER_PASSWORD;
                user.setLogin(text);
                setMessageProperty(result,chatId, "Пароль: ");
                resultMessages.add(result);
                return resultMessages;

            case ENTER_PASSWORD:
                botStatus = Status.NOTHING;
                user.setPassword(text);
                enterUser(result,chatId);
                resultMessages.add(result);
                return resultMessages;

            default:
                throw new TelegramApiException("Такое состояние у бота " + botStatus.name() + " отсуствует !");
        }
    }


    private void сommandExecutionBot(long chatId,String text,SendMessage result) throws TelegramApiException {

        switch (text){

            case "/start":
                log.info("Бот запущен !");
                setMessageProperty(result,chatId,"Добро пожаловать");
                break;

            case "/help":
                log.info("Выбрана команда '/help'");
                setMessageProperty(result,chatId, BotCommands.HELP.getMessageTextSelect());
                break;

            case "/weather":

            case "Узнать погоду \uD83D\uDDD3":
                botStatus = Status.ASK_CITY;
                log.info("Выбрана команда 'Узнать погоду'");
                setMessageProperty(result,chatId,"Выберите город: ");
                break;

            case "Войти в профиль \uD83D\uDD10":
                log.info("Выбрана команда 'Войти в профиль'");
                userIsEnter(result,chatId);
                break;

            case "Создать профиль \uD83D\uDC64":
                log.info("Выбрана команда 'Создать профиль'");
                inlineKeyboardGenerate.setInlineKeyboard(result);
                setMessageProperty(result,chatId,"Вы хотите создать профиль ?");
                break;

            case "Посмотреть избранное ✅":
                log.info("Выбрана команда 'Посмотреть избранное'");
                resultMessages = getListWeatherCityFavorites(chatId);
                break;

            default:
                throw new TelegramApiException("Данная команда " + text + " не существует");
        }


    }


    private void setMessageProperty(SendMessage message,long chatId,String text){
        message.setText(text);
        message.setChatId(chatId);
    }

    public String getSelectedCity(){
        return weatherService.getChoiceCity();
    }


    private boolean checkIsEmptyUser(){
        if (user != null){
            return user.getCity() == null
                    || user.getPassword() == null
                    || user.getLogin() == null
                    || user.getId() == null;
        }
        else {
            return true;
        }
    }

    private void userIsEnter(SendMessage result,long chatId){
        if (!checkIsEmptyUser()){
            setMessageProperty(result,chatId,"Вы уже зашли в свой профиль. Ваш профиль: " + user.getLogin());
        } else {
            user = com.telegrambot.BotWeather.model.User.getUser();
            botStatus = Status.ENTER_LOGIN;
            setMessageProperty(result,chatId,"Логин: ");
        }
    }

    private void registerUser(SendMessage result,long chatId) throws TelegramApiException{
        Query query = new Query();
        query.addCriteria(Criteria.where("login").is(user.getLogin()));
        List<User> users = mongoTemplate.find(query, User.class);
        try {
            if (users.isEmpty()){
                userRepository.insert(user);
                setMessageProperty(result,chatId,"Профиль создан успешно");
            } else {
                user.clearInfoUser();
                throw new TelegramApiException("Такой пользователь уже был создан");
            }
        } catch (org.springframework.dao.DuplicateKeyException ex){
            user.clearInfoUser();
            throw new TelegramApiException("Ошибка работы с MongoDB: " + ex.getMessage());
        }
    }

    private void enterUser(SendMessage result,long chatId) throws TelegramApiException{
        Query query = new Query();
        query.addCriteria(Criteria.where("login").is(user.getLogin()).and("password").is(user.getPassword()));
        List<User> users = mongoTemplate.find(query, User.class);
        try {
            if (users.size() != 1){
                user.clearInfoUser();
                throw new TelegramApiException("Такой пользователь возможно не создан или неверно указан пароль !");
            }
            else {
                user.updateInfoUser(users);
                setMessageProperty(result,chatId,"Вход в профиль произошло успешно !");
            }
        } catch (RuntimeException ex){
            user.clearInfoUser();
            throw new TelegramApiException("Ошибка работы телеграм бота при входе профиля: " + ex.getMessage());
        }
    }

    public List<SendMessage> getListWeatherCityFavorites(long chatId) throws TelegramApiException {
        List<SendMessage> listMessage = new ArrayList<>();
        if (!checkIsEmptyUser()){
            if (user.getCity().size() != 0){
                List<FavoritesCity> cityList = user.getCity();
                for (int i = 0; i < cityList.size();i++){
                    SendMessage resultWeather = new SendMessage();
                    String resultQuery = weatherService.weatherFavoriteCity(cityList.get(i).getCity());
                    setMessageProperty(resultWeather,chatId,resultQuery);
                    inlineKeyboardGenerate.setInlineKeyboardDelFavoriteCity(resultWeather);
                    listMessage.add(resultWeather);
                }
            }
            else {
                SendMessage message = new SendMessage();
                setMessageProperty(message,chatId,"Ваш список избранного пуст !");
                listMessage.add(message);
            }
            return listMessage;
        } else {
            throw new TelegramApiException("Вы не вошли в свой профиль, пожалуйста войдите в свой профиль");
        }
    }



}
