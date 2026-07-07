package com.cricketteam.app.cricketteam.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cricketteam.app.cricketteam.model.Player;
import com.cricketteam.app.cricketteam.repository.PlayerRepository;
import com.cricketteam.app.cricketteam.view.PlayerView;

@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerTest {

	@Mock
	private PlayerRepository playerRepository;

	@Mock
	private PlayerView playerView;

	@InjectMocks
	private PlayerController playerController;

	@Test
	public void testAllPlayers() {
		List<Player> players = Arrays.asList(new Player("1", "Junaid Munir", "Batsman"));
		when(playerRepository.findAll()).thenReturn(players);

		playerController.allPlayers();

		verify(playerView).showAllPlayers(players);
	}

	@Test
	public void testNewPlayer() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerController.newPlayer(player);
		verify(playerRepository).save(player);
		verify(playerView).playerAdded(player);
	}

	@Test
	public void testDeletePlayer() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerController.deletePlayer(player);
		verify(playerRepository).delete("1");
		verify(playerView).playerRemoved(player);
	}

	@Test
	public void testUpdatePlayerShouldDelegateToRepositoryAndNotifyView() {
		Player player = new Player("1", "Junaid Munir", "Captain");
		playerController.updatePlayer(player);
		verify(playerRepository).update(player);
		verify(playerView).playerUpdated(player);
	}
}


