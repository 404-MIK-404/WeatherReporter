package com.telegrambot.BotWeather.service.message.url;

import com.telegrambot.BotWeather.config.property.OpenWeatherConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherForecastInfoUrl {

    @Autowired
    private OpenWeatherConfig openWeatherConfig;

    private String unit = "metric";

    private final OkHttpClient client = new OkHttpClient();

    public Response getResultUrlWeatherCity(String city) throws IOException {
        Request request = new Request.Builder().url("" +
                "http://api.openweathermap.org/data/2.5/weather?q="+ city +"&units="+unit+"&appid=" + openWeatherConfig.getApiKey())
                .build();
        return client.newCall(request).execute();
    }


    public Response getResultUrlWeatherCityForecast(String city) throws IOException {
        Request request = new Request.Builder().url("" +
                "http://api.openweathermap.org/data/2.5/forecast/?q="+ city +"&units="+unit+"&appid=" + openWeatherConfig.getApiKey())
                .build();
        return client.newCall(request).execute();
    }



}
