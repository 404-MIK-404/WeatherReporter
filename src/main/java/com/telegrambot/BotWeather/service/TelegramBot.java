package com.telegrambot.BotWeather.service;

import com.telegrambot.BotWeather.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config){
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getApiTokenBot();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
                String command = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                if ("/start".equals(command)){
                    sendMessage(chatId,"This bot is working !");
                    log.info("Bot is working");
                } else {
                    sendMessage(chatId,"Hello " + command);
                    log.info("Send command is successfully");
                }
        }
    }

    private void sendMessage(long chatId,String text)
    {
        try {
            SendMessage message = new SendMessage();
            message.setText("Hello " + text);
            message.setChatId(chatId);
            execute(message);
        } catch (TelegramApiException exception){
            log.error(exception.getMessage());

        }
    }






}
