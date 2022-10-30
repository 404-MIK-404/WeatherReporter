package com.telegrambot.BotWeather.service.message.formatMessage;

import com.telegrambot.BotWeather.weather.WeatherIndicators;
import com.telegrambot.BotWeather.weather.WeatherSystem;
import com.telegrambot.BotWeather.weather.WeatherWind;



import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WeatherMessageFormatter {


    private WeatherSystem weatherSystem;

    private WeatherWind weatherWind;

    private WeatherIndicators weatherIndicators;


    public WeatherMessageFormatter(WeatherSystem weatherSystem,WeatherWind weatherWind,WeatherIndicators weatherIndicators){
        this.weatherSystem = weatherSystem;
        this.weatherWind = weatherWind;
        this.weatherIndicators = weatherIndicators;
    }

    private String getZonedTime(Integer secs, Integer zoneSecs) {
        Instant instant = Instant.ofEpochSecond(secs);
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(zoneSecs);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneOffset);
        return zonedDateTime.toLocalTime().toString();
    }

    public String returnMessageWeatherCity(String selectCity)
    {
        String textResult = String.format(
                "Город: \t %s \t %s \n" +
                        "----------------------- \n" +
                "\uD83C\uDF21 Температура: %s°C\n" +
                "Температура по ощущению: %s°C\n\n" +
                "\uD83C\uDF90Ветер: %s м/с\n" +
                "♨ Давление: %s мм рт.ст.\n" +
                "\uD83D\uDCA7 Относительная влажность: %s %%\n\n" +
                "\uD83C\uDF05 Рассвет: %s\n" +
                "\uD83C\uDF07 Закат: %s\n",selectCity,
                weatherSystem.getCountryCode().getEmoji(),
                weatherIndicators.getTemperature(),
                weatherIndicators.getTemperatureFeeling(),
                weatherWind.getSpeed(),
                weatherIndicators.getPressure() * 3 / 4,
                weatherIndicators.getHumidity(),
                getZonedTime(weatherSystem.getSunriseEpochSeconds(),weatherSystem.getTimeZone()),
                getZonedTime(weatherSystem.getSunsetEpochSeconds(),weatherSystem.getTimeZone())
        );
        return textResult;
    }


    public String returnMessageFavoriteCity(String selectCity){
        String textResult = String.format(
                "Город: \t %s \t %s \n" +
                        "----------------------- \n" +
                        "\uD83C\uDF21 Температура: %s°C\n" +
                        "Ощущается как: %s°C\n", selectCity,
                weatherSystem.getCountryCode().getEmoji(),
                weatherIndicators.getTemperature(),
                weatherIndicators.getTemperatureFeeling()
        );
        return textResult;
    }





}
