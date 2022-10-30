package com.telegrambot.BotWeather.service.message.formatMessage;

import com.telegrambot.BotWeather.forecast.ForecastIndicators;

import java.text.DecimalFormat;

public class ForecastMessageFormatter {


    private ForecastIndicators forecastIndicators;


    public ForecastMessageFormatter(ForecastIndicators forecastIndicators) {
        this.forecastIndicators = forecastIndicators;
    }


    public String toString(String city) {
        DecimalFormat f = new DecimalFormat("##.00");
        String textResult = String.format(
                "Погода на завтра \uD83D\uDDD3 \n" +
                "Город: \t %s \n" +
                        "--------------------------------------------------- \n" +
                "\uD83D\uDD25 Максимально средняя температура: \t %s °C\n" +
                "❄ Минимально средняя температура: \t %s °C\n" +
                "Средняя температура по ощущению: \t %s °C\n\n" +
                "♨ Среднее давление: \t %s мм рт.ст.\n" +
                "\uD83D\uDCA7 Средняя относительная влажность: \t %s %%\n",
                city,
                f.format(forecastIndicators.getMaxTemperatureAvg()),
                f.format(forecastIndicators.getMinTemperatureAvg()),
                f.format(forecastIndicators.getTemperatureFeelingAvg()),
                forecastIndicators.getPressureAvg(),
                forecastIndicators.getHumidityAvg()
        );
        return textResult;
    }
}
