package com.telegrambot.BotWeather.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForecastIndicators {


    private Float maxTemperatureAvg; // Макс. Температура °C

    private Float minTemperatureAvg; // Мин. Температура °C

    private Float temperatureFeelingAvg; //Температура по ощущению °C

    private Integer pressureAvg; //Давление гПа

    private Integer humidityAvg; //Влажность




}
