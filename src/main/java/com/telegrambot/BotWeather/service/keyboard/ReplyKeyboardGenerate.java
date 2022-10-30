package com.telegrambot.BotWeather.service.keyboard;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReplyKeyboardGenerate {

    private List<KeyboardRow> keyboardRows = new ArrayList<>();

    private KeyboardRow row = new KeyboardRow();

    private List<String> list = Arrays.asList("Узнать погоду \uD83D\uDDD3","Посмотреть избранное ✅","Создать профиль \uD83D\uDC64", "Войти в профиль \uD83D\uDD10");

    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    public void setBotReplyKeyboard(SendMessage message){
        row.clear();
        keyboardRows.clear();
        for (int i = 0; i < list.size();i++){
            row = new KeyboardRow();
            row.add(list.get(i));
            keyboardRows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        message.setReplyMarkup(replyKeyboardMarkup);
    }






}
