package com.telegrambot.BotWeather.service.keyboard;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InlineKeyboardGenerate {

    private InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
    private List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
    private List<InlineKeyboardButton> rowInLine = new ArrayList<>();

    /** Кнопки для регистраций пользователя */
    private List<String> nameCallbackDataRegistration = Arrays.asList("YES_BUTTON_REGISTRATION","NO_BUTTON_REGISTRATION");
    private List<String> nameButtonDataRegistration = Arrays.asList("Да ✅","Нет ❌");

    /** Кнопка для удаление городов из избранного списка */
    private List<String> nameCallbackDataDelFavoriteCity = Arrays.asList("BUTTON_REMOVE_FAVORITE_CITY");
    private List<String> nameButtonDataDelFavoriteCity = Arrays.asList("Удалить из избранного ❌");


    /**Кнопки для просмотра погоды на завтра*/
    private List<String> nameCallbackDataWeatherForecast = Arrays.asList("FAVORITE_BUTTON","BUTTON_TOMORROW_WEATHER_CITY");
    private List<String> nameButtonDataWeatherForecast = Arrays.asList("Добавить в избранное ✅","Узнать погоду на завтра \uD83D\uDDD3");


    public void setInlineKeyboard(SendMessage message){
        rowsInLine.clear();
        rowInLine.clear();
        for (int i = 0; i < nameCallbackDataRegistration.size();i++){
            InlineKeyboardButton buttonCreate = new InlineKeyboardButton();
            buttonCreate.setText(nameButtonDataRegistration.get(i));
            buttonCreate.setCallbackData(nameCallbackDataRegistration.get(i));
            rowInLine.add(buttonCreate);
        }
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
    }


    public void setInlineKeyboardDelFavoriteCity(SendMessage message){
        rowsInLine.clear();
        rowInLine.clear();
        for (int i = 0; i < nameCallbackDataDelFavoriteCity.size();i++){
            InlineKeyboardButton buttonFavorite = new InlineKeyboardButton();
            buttonFavorite.setText(nameButtonDataDelFavoriteCity.get(i));
            buttonFavorite.setCallbackData(nameCallbackDataDelFavoriteCity.get(i));
            rowInLine.add(buttonFavorite);
        }
        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
    }

    public void setInlineKeyboardForecastWeatherCity(SendMessage message){
        rowsInLine.clear();
        rowInLine.clear();
        List<List<InlineKeyboardButton>> weatherButtons = new ArrayList<>();
        for (int i = 0; i < nameButtonDataWeatherForecast.size();i++){
            weatherButtons.add(new ArrayList<>());
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(nameButtonDataWeatherForecast.get(i));
            inlineKeyboardButton.setCallbackData(nameCallbackDataWeatherForecast.get(i));
            weatherButtons.get(weatherButtons.size()-1).add(inlineKeyboardButton);
        }
        rowsInLine.addAll(weatherButtons);
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
    }



}
