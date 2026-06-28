package com.cricketteam.app.cricketteam.repository;

import java.util.List;

import com.cricketteam.app.cricketteam.model.Player;

public interface PlayerRepository {
    List<Player> findAll();
    Player findById(String id);
    void save(Player player);
    void delete(String id);
}
