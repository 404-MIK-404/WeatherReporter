package com.telegrambot.BotWeather.service;

import com.telegrambot.BotWeather.forecast.ForecastIndicators;
import com.telegrambot.BotWeather.service.handler.CommandHandle;
import com.telegrambot.BotWeather.service.message.formatMessage.ForecastMessageFormatter;
import com.telegrambot.BotWeather.service.message.url.WeatherForecastInfoUrl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ForecastService {


    private ForecastIndicators forecastIndicators;

    private ForecastMessageFormatter forecastMessageFormatter;

    @Autowired
    private WeatherForecastInfoUrl forecastInfo;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void setForecastIndicators(List<JSONObject> jsonList){
        float tempMaxAvg = 0;
        float tempMinAvg = 0;
        float tempFeelsLike = 0;
        int pressureAvg = 0;
        int humidity = 0;

        for (int i = 0; i < jsonList.size();i++){
            JSONObject objectMain = jsonList.get(i).getJSONObject("main");
            tempMaxAvg += Float.parseFloat(objectMain.get("temp_max").toString());
            tempMinAvg += Float.parseFloat(objectMain.get("temp_min").toString());
            tempFeelsLike += Float.parseFloat(objectMain.get("feels_like").toString());
            pressureAvg += Float.parseFloat(objectMain.get("pressure").toString());
            humidity += Float.parseFloat(objectMain.get("humidity").toString());
        }
        forecastIndicators = new ForecastIndicators();
        forecastIndicators.setMaxTemperatureAvg(tempMaxAvg / jsonList.size());
        forecastIndicators.setMinTemperatureAvg(tempMinAvg / jsonList.size());
        forecastIndicators.setTemperatureFeelingAvg(tempFeelsLike / jsonList.size());
        forecastIndicators.setPressureAvg(pressureAvg / jsonList.size());
        forecastIndicators.setHumidityAvg(humidity / jsonList.size());
    }


    public String getForecastMessage(String city){
        forecastMessageFormatter = new ForecastMessageFormatter(forecastIndicators);
        return forecastMessageFormatter.toString(city);
    }

    public String getTomorrowWeatherCity(CommandHandle commandHandle, long chatId) throws TelegramApiException{
        try{
            Response resultRequest = forecastInfo.getResultUrlWeatherCityForecast(commandHandle.getSelectedCity());

            Calendar dateNow = getCalendarNow();
            JSONObject jsonWeather = new JSONObject(resultRequest.body().string());
            JSONArray jsonWeatherLists =  jsonWeather.getJSONArray("list");
            List<JSONObject> jsonList = new ArrayList<>();
            for (int i = 0; i < jsonWeatherLists.length();i++){
                jsonList.add(jsonWeatherLists.getJSONObject(i));
            }
            jsonList  = jsonList.stream().filter((e)->{
                String dataWeather = e.get("dt_txt").toString();
                return filterWeatherJSON(dataWeather,dateNow);
            }).collect(Collectors.toList());
            setForecastIndicators(jsonList);
            return getForecastMessage(commandHandle.getSelectedCity());
        } catch (IOException ex){
            throw new TelegramApiException("Произошла ошибка !");
        }
    }

    private Calendar getCalendarNow(){
        Calendar dateNow = Calendar.getInstance();
        dateNow.set(Calendar.HOUR,0);
        dateNow.set(Calendar.MINUTE,0);
        dateNow.set(Calendar.SECOND,0);
        dateNow.add(Calendar.DAY_OF_MONTH,1);
        return dateNow;
    }

    private boolean dateCheck(Calendar dateNow,Calendar dateJSON){
        return dateJSON.get(Calendar.DAY_OF_MONTH) != dateNow.get(Calendar.DAY_OF_MONTH)
                || dateJSON.get(Calendar.MONTH) != dateNow.get(Calendar.MONTH)
                || dateJSON.get(Calendar.YEAR) != dateNow.get(Calendar.YEAR);
    }

    private boolean filterWeatherJSON(String dataWeather,Calendar dateNow){
        try {
            Calendar calendarDataWeather = Calendar.getInstance();
            calendarDataWeather.setTime(sdf.parse(dataWeather));
            return dateCheck(dateNow,calendarDataWeather);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return false;
        }
    }



}
