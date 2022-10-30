package com.telegrambot.BotWeather.service.handler;


import com.telegrambot.BotWeather.model.FavoritesCity;
import com.telegrambot.BotWeather.repository.UserRepository;
import com.telegrambot.BotWeather.service.ForecastService;
import com.telegrambot.BotWeather.service.keyboard.ReplyKeyboardGenerate;
import com.telegrambot.BotWeather.enums.Status;
import com.telegrambot.BotWeather.service.message.url.WeatherForecastInfoUrl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
@Slf4j
public class CallbackHandle {

    @Autowired
    private ReplyKeyboardGenerate replyKeyboardGenerate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ForecastService forecastService;


    private SendMessage resultMessage;

    private com.telegrambot.BotWeather.model.User user ;

    public SendMessage useButtonCallback(CommandHandle commandHandle,Update update) throws TelegramApiException {

        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String cityNeedRemoveFromFavorite = update.getCallbackQuery().getMessage().getText();

        resultMessage = new SendMessage();
        switch (callbackData){
            case "YES_BUTTON_REGISTRATION":
                return userIsCreated(commandHandle,chatId);

            case "NO_BUTTON_REGISTRATION":
                return setMessageProperty(chatId,"Отмена создания пользователя !");

            case "FAVORITE_BUTTON":
                return addFavoriteCity(commandHandle,chatId);

            case "BUTTON_REMOVE_FAVORITE_CITY":
                return deleteCurrentFavoriteCity(chatId,cityNeedRemoveFromFavorite);

            case "BUTTON_TOMORROW_WEATHER_CITY":
                return getWeatherTomorrow(commandHandle,chatId);

            default:
                throw new TelegramApiException("Ошибка при работе ");
        }

    }

    private SendMessage setMessageProperty(long chatId,String text){
        resultMessage.setText(text);
        resultMessage.setChatId(chatId);
        return resultMessage;
    }

    private SendMessage userIsCreated(CommandHandle commandHandle,long chatId){
        user = com.telegrambot.BotWeather.model.User.getUserCreate();
        if (!checkIsEmptyUser()){
            return setMessageProperty(chatId,"Вы уже зашли в свой профиль. Ваш профиль: " + user.getLogin());
        } else {
            user = com.telegrambot.BotWeather.model.User.getUser();
            commandHandle.setUser(user);
            commandHandle.setBotStatus(Status.CREATE_LOGIN);
            return setMessageProperty(chatId,"Придумайте логин пользователя: ");
        }
    }

    private SendMessage addFavoriteCity(CommandHandle commandHandle,long chatId){
        user = com.telegrambot.BotWeather.model.User.getUserCreate();
        if (!checkIsEmptyUser()){
            List<FavoritesCity> cityList = user.getCity();
            String selectedCity = commandHandle.getSelectedCity();
            if (!cityList.stream().filter(o->o.getCity().equals(selectedCity)).findFirst().isPresent()){
                user.getCity().add(new FavoritesCity(commandHandle.getSelectedCity()));
                userRepository.save(user);
            } else {
                return setMessageProperty(chatId,"Даный город был уже добавлен !");
            }
            return setMessageProperty(chatId,"Даный город добавлен в избранное !");
        } else {
            return setMessageProperty(chatId,"Невозможно добавить данный город в израбнное, необходимо войти в профиль !");
        }
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

    private SendMessage deleteCurrentFavoriteCity(long chatId,String findCity) throws TelegramApiException{
        user = com.telegrambot.BotWeather.model.User.getUserCreate();
        List<FavoritesCity> cityList = user.getCity();
        int res = IntStream.range(0,cityList.size())
                .filter(i->findCity.contains(cityList.get(i).getCity()))
                .findFirst()
                .orElse(-1);
        if (res != -1){
            cityList.remove(res);
            userRepository.save(user);
            return setMessageProperty(chatId,"Город из избранного удалён !");
        } else {
           throw new TelegramApiException("Ошибка при удалений города из списка избранного");
        }
    }



    private SendMessage getWeatherTomorrow(CommandHandle commandHandle,long chatId) throws TelegramApiException{
        return setMessageProperty(chatId,forecastService.getTomorrowWeatherCity(commandHandle,chatId));
    }







}
