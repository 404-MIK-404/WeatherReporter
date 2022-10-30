package com.telegrambot.BotWeather.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherIndicators {

    @JsonProperty("temp")
    private Float temperature; // Температура °C

    @JsonProperty("feels_like")
    private Float temperatureFeeling; //Температура по ощущению °C

    private Integer pressure; //Давление гПа

    private Integer humidity; //Влажность

}
