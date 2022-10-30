package com.telegrambot.BotWeather.config.property;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Data
@PropertySource("application.properties")
@Getter
public class OpenWeatherConfig {
    @Value("${openWeather.key.api}")
    private String apiKey;

}