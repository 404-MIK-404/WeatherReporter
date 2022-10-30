package com.telegrambot.BotWeather.service.menu;

import com.telegrambot.BotWeather.enums.BotCommands;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.List;

@Component
public class MenuBot {

    @Getter
    private static List<BotCommand> listCommands = Arrays.asList(
            new BotCommand(BotCommands.START.getCommand(), BotCommands.START.getDescription()),
            new BotCommand(BotCommands.HELP.getCommand(), BotCommands.HELP.getDescription()),
            new BotCommand(BotCommands.WEATHER.getCommand(), BotCommands.WEATHER.getDescription())
            );



}
