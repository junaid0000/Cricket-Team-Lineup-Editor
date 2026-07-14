package com.cricketteam.app.cricketteam.controller;

import com.cricketteam.app.cricketteam.model.Player;
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

	public void newPlayer(Player player) {
		if (playerRepository.findById(player.getId()) != null) {
			playerView.showError("Already exists with ID " + player.getId(), player);
			return;
		}
		playerRepository.save(player);
		playerView.playerAdded(player);
	}

	public void deletePlayer(Player player) {
		if (playerRepository.findById(player.getId()) == null) {
			playerView.showError("No player exists with ID " + player.getId(), player);
			return;
		}
		playerRepository.delete(player.getId());
		playerView.playerRemoved(player);
	}

	public void updatePlayer(Player player) {
		if (playerRepository.findById(player.getId()) == null) {
			playerView.showError("No player exists with ID " + player.getId(), player);
			return;
		}
		playerRepository.update(player);
		playerView.playerUpdated(player);
	}
}


