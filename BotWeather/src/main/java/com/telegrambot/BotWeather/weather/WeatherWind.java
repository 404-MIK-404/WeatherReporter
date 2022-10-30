package com.telegrambot.BotWeather.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherWind {

    private Float speed;

    @JsonProperty("deg")
    private Integer degree;

}
