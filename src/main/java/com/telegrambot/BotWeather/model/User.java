package com.telegrambot.BotWeather.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Document
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String login;
    private String password;
    private List<FavoritesCity> city = new ArrayList<>();


    private static User user;

    private User(String id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    private User() {
    }

    public static User getUser(){
        if (user == null){
            user = new User();
        }
        return user;
    }

    public static User getUserCreate(){
        return user;
    }

    public void updateInfoUser(List<User> users){
        user.setId(users.get(0).getId());
        user.setLogin(users.get(0).getLogin());
        user.setPassword(users.get(0).getPassword());
        user.setCity(users.get(0).getCity());
    }

    public void clearInfoUser(){
        user.setId(null);
        user.setPassword(null);
        user.setLogin(null);
        user.setCity(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", city=" + city +
                '}';
    }
}
