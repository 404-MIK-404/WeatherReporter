package com.telegrambot.BotWeather.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Getter
@Setter
@Document
public class FavoritesCity {

    @Id
    private String id;
    private String city;

    public FavoritesCity(String city) {
        this.city = city;
    }


}
