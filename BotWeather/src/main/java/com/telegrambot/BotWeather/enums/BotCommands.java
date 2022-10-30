package com.telegrambot.BotWeather.enums;

import lombok.Getter;

@Getter
public enum BotCommands {


    HELP("/help",
            "Возвращает список команд и описание этих команд",
            "Этот бот был создан для отображения погоды в разных городах.\n\n" +
                    "Вы можете ввести комманды с помощью клавиатуры или с помощью меню, список комманд: \n\n" +
                    "Команда /start выдаст привествующее сообщение и о том что бот начал свою работу\n\n" +
                    "Команда /weather выведет погоду города который вы хотите узнать\n\n" +
                    "Команда /help отправит это же сообщение"),

    START("/start",
            "Запускает и возвращает сообщение о начале работе бота",
            "Добро пожаловать !"),

    WEATHER("/weather","Отображает данные о погоде в выбранном городе")
    ;


    private String command;
    private String description;
    private String messageTextSelect;

    BotCommands(String command, String description,String messageTextSelect) {
        this.command = command;
        this.description = description;
        this.messageTextSelect = messageTextSelect;
    }

    BotCommands(String command, String description) {
        this.command = command;
        this.description = description;
    }


}
