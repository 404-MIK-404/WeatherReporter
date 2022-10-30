package com.telegrambot.BotWeather.service.bot;


import com.telegrambot.BotWeather.config.property.BotConfig;
import com.telegrambot.BotWeather.service.menu.MenuBot;
import com.telegrambot.BotWeather.service.message.formatMessage.MessageBotFormatter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;


    @Autowired
    private MessageBotFormatter messageFormatter;

    public TelegramBot(BotConfig config){
        this.config = config;
        initListCommand();
    }


    private void initListCommand(){
        try {
            this.execute(new SetMyCommands(MenuBot.getListCommands(),new BotCommandScopeDefault(),null));
        } catch (TelegramApiException ex){
            log.error("Ошибка при работе с списком команд !" + ex.getMessage());
        }
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
        try {
            if (update.hasMessage() && update.getMessage().hasText()){
                List<SendMessage> resultMessage = messageFormatter.getResultMessageCommand(update);
                for (int i = 0; i < resultMessage.size();i++){
                    execute(resultMessage.get(i));
                }
            } else if (update.hasCallbackQuery()){
                execute(messageFormatter.getResultMessageCallback(update));
            }
        } catch (TelegramApiException ex){
            log.error("Ошибка при выполнение действий ", ex.getMessage());
            ErrorMessage(update.getMessage().getChatId(),ex.getMessage());
        }
        initListCommand();
    }


    private void ErrorMessage(long chatId,String text){
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            execute(message);
        } catch (TelegramApiException ex){
            log.error("Ошибка при выполнение команды: ", ex.getMessage());
            ErrorMessage(chatId, "Произошла ошибка при выводе сообщения !");
        }
    }



}
