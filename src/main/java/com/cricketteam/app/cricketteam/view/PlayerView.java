package com.cricketteam.app.cricketteam.view;

import java.util.List;

import com.cricketteam.app.cricketteam.model.Player;

public interface PlayerView {
	void showAllPlayers(List<Player> players);
	void playerAdded(Player player);
	void playerRemoved(Player player);
	void playerUpdated(Player player);
}


