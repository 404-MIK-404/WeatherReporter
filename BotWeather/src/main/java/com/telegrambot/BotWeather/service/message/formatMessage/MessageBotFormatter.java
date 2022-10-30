package com.telegrambot.BotWeather.service.message.formatMessage;


import com.telegrambot.BotWeather.service.handler.CallbackHandle;
import com.telegrambot.BotWeather.service.handler.CommandHandle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MessageBotFormatter {

    @Autowired
    private CallbackHandle callbackHandle;

    @Autowired
    private CommandHandle commandHandle;



    public List<SendMessage> getResultMessageCommand(Update update) throws TelegramApiException {
        List<SendMessage> messageNeedSend = new ArrayList<>();
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        messageNeedSend = commandHandle.statusNowBot(chatId,text);
        return messageNeedSend;
    }




    public SendMessage getResultMessageCallback(Update update) throws TelegramApiException{
        SendMessage sendMessage = callbackHandle.useButtonCallback(commandHandle,update);
        return sendMessage;
    }





}
