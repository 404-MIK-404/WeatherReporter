package com.telegrambot.BotWeather.repository;

import com.telegrambot.BotWeather.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
}
