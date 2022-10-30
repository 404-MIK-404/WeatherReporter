package com.telegrambot.BotWeather.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telegrambot.BotWeather.enums.CountryEmoji;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherSystem {

    private CountryEmoji countryCode;

    @JsonProperty("sunrise")
    private Integer sunriseEpochSeconds;

    @JsonProperty("sunset")
    private Integer sunsetEpochSeconds;

    private Integer timeZone;


}
