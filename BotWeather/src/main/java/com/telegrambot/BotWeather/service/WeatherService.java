package com.telegrambot.BotWeather.service;


import com.telegrambot.BotWeather.service.keyboard.InlineKeyboardGenerate;
import com.telegrambot.BotWeather.weather.WeatherIndicators;
import com.telegrambot.BotWeather.weather.WeatherSystem;
import com.telegrambot.BotWeather.weather.WeatherWind;
import com.telegrambot.BotWeather.enums.CountryEmoji;
import com.telegrambot.BotWeather.service.message.url.WeatherForecastInfoUrl;
import com.telegrambot.BotWeather.service.message.formatMessage.WeatherMessageFormatter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


@Service
@Slf4j
public class WeatherService {


    @Autowired
    private WeatherForecastInfoUrl weatherResultURL;

    private Response response;

    private JSONObject resultResponse;


    private WeatherSystem weatherSystem;
    private WeatherWind weatherWind;
    private WeatherIndicators weatherIndicators;


    private WeatherMessageFormatter messageWeatherFormatter;


    @Getter
    private String choiceCity;


    public String weatherCity(String choiceCity, InlineKeyboardGenerate inlineKeyboardGenerate, SendMessage message) throws TelegramApiException {
        try {
            response = weatherResultURL.getResultUrlWeatherCity(choiceCity);
            if (response.code() != 404){
                this.choiceCity = choiceCity;
                resultResponse = new JSONObject(response.body().string());
                this.messageWeatherFormatter = setWeatherCity();
                return messageWeatherFormatter.returnMessageWeatherCity(choiceCity);
            } else {
                clearWeather();
                throw new TelegramApiException("Такого города под названием "+ choiceCity +" не существует ");
            }
        } catch (IOException | JSONException ex) {
            clearWeather();
            log.error("ERROR: " + ex.fillInStackTrace());
            throw new TelegramApiException("Произошла ошибка при поиске города !");
        }
    }


    public String weatherFavoriteCity(String choiceCity) throws TelegramApiException
    {
        try {
            response = weatherResultURL.getResultUrlWeatherCity(choiceCity);
            if (response.code() != 404){
                resultResponse = new JSONObject(response.body().string());
                this.messageWeatherFormatter = setWeatherFavoriteCity();
                return messageWeatherFormatter.returnMessageFavoriteCity(choiceCity);
            } else {
                clearWeather();
                throw new TelegramApiException("Такого города под названием "+ choiceCity +" не существует ");
            }
        } catch (IOException | JSONException ex) {
            clearWeather();
            log.error("ERROR: " + ex.fillInStackTrace());
            throw new TelegramApiException("Произошла ошибка при поиске города !");
        }


    }

    private WeatherMessageFormatter setWeatherFavoriteCity(){
        setWeatherIndicators();
        setWeatherSystem();
        return new WeatherMessageFormatter(weatherSystem,null,weatherIndicators);
    }

    private WeatherMessageFormatter setWeatherCity() {
        setWeatherIndicators();
        setWeatherSystem();
        setWeatherWind();
        return new WeatherMessageFormatter(weatherSystem,weatherWind,weatherIndicators);
    }

    private void setWeatherIndicators() throws JSONException {
        JSONObject weatherJsonObject = resultResponse.getJSONObject("main");
        this.weatherIndicators = new WeatherIndicators();
        weatherIndicators.setHumidity(Integer.parseInt(weatherJsonObject.get("humidity").toString()));
        weatherIndicators.setTemperature(Float.parseFloat(weatherJsonObject.get("temp").toString()));
        weatherIndicators.setPressure(Integer.parseInt(weatherJsonObject.get("pressure").toString()));
        weatherIndicators.setTemperatureFeeling(Float.parseFloat(weatherJsonObject.get("feels_like").toString()));
    }

    private void setWeatherSystem() throws JSONException {
        JSONObject systemJsonObject = resultResponse.getJSONObject("sys");
        this.weatherSystem = new WeatherSystem();
        weatherSystem.setCountryCode(CountryEmoji.getByName(systemJsonObject.get("country").toString()));
        weatherSystem.setSunriseEpochSeconds(Integer.parseInt(systemJsonObject.get("sunrise").toString()));
        weatherSystem.setSunsetEpochSeconds(Integer.parseInt(systemJsonObject.get("sunset").toString()));
        weatherSystem.setTimeZone(Integer.parseInt(resultResponse.get("timezone").toString()));

    }

    private void setWeatherWind() throws JSONException {
        JSONObject windJsonObject = resultResponse.getJSONObject("wind");
        this.weatherWind = new WeatherWind();
        weatherWind.setDegree(Integer.parseInt(windJsonObject.get("deg").toString()));
        weatherWind.setSpeed(Float.parseFloat(windJsonObject.get("speed").toString()));
    }

    private void clearWeather(){
        weatherSystem = null;
        weatherWind = null;
        weatherIndicators = null;
    }

}
