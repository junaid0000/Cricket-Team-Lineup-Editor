package com.cricketteam.app.cricketteam.controller;

import com.cricketteam.app.cricketteam.repository.PlayerRepository;
import com.cricketteam.app.cricketteam.view.PlayerView;

public class PlayerController {

    private PlayerView playerView;
    private PlayerRepository playerRepository;

    public PlayerController(PlayerView playerView, PlayerRepository playerRepository) {
        this.playerView = playerView;
        this.playerRepository = playerRepository;
    }

    public void allPlayers() {
        playerView.showAllPlayers(playerRepository.findAll());
    }
}
