package com.cricketteam.app.cricketteam.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.cricketteam.app.cricketteam.model.Player;
import com.cricketteam.app.cricketteam.repository.PlayerMongoRepository;
import com.cricketteam.app.cricketteam.view.PlayerView;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * To run this test from Eclipse, a Docker daemon/Docker Desktop must be running on the host machine.
 * Testcontainers will automatically spin up the required MongoDB container.
 */
public class PlayerControllerIT {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

	@Mock
	private PlayerView playerView;

	private MongoClient client;
	private PlayerMongoRepository playerRepository;
	private PlayerController playerController;
	private AutoCloseable closeable;

	private static final String CRICKET_DB_NAME = "cricket";
	private static final String PLAYER_COLLECTION_NAME = "player";

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
		client = MongoClients.create(mongo.getReplicaSetUrl());
		client.getDatabase(CRICKET_DB_NAME).drop();
		playerRepository = new PlayerMongoRepository(client, CRICKET_DB_NAME, PLAYER_COLLECTION_NAME);
		playerController = new PlayerController(playerView, playerRepository);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
		closeable.close();
	}

	@Test
	public void testAllPlayersShowsRepositoryPlayers() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerRepository.save(player);
		playerController.allPlayers();
		verify(playerView).showAllPlayers(List.of(player));
	}

	@Test
	public void testNewPlayerPersistsPlayer() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerController.newPlayer(player);
		assertThat(playerRepository.findById("1"))
			.usingRecursiveComparison()
			.isEqualTo(player);
		verify(playerView).playerAdded(player);
	}

	@Test
	public void testUpdatePlayerPersistsChanges() {
		playerRepository.save(new Player("1", "Junaid Munir", "Batsman"));
		Player editedPlayer = new Player("1", "Junaid Munir", "Captain");
		playerController.updatePlayer(editedPlayer);
		assertThat(playerRepository.findById("1"))
			.usingRecursiveComparison()
			.isEqualTo(editedPlayer);
		verify(playerView).playerUpdated(editedPlayer);
	}

	@Test
	public void testDeletePlayerRemovesPlayer() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerRepository.save(player);
		playerController.deletePlayer(player);
		assertThat(playerRepository.findById("1")).isNull();
		verify(playerView).playerRemoved(player);
	}

	@Test
	public void testNewPlayerWhenAlreadyExistsShouldShowError() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerRepository.save(player);
		playerController.newPlayer(player);
		verify(playerView).showError("Already exists with ID 1", player);
	}

	@Test
	public void testUpdatePlayerWhenDoesNotExistShouldShowError() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerController.updatePlayer(player);
		verify(playerView).showError("No player exists with ID 1", player);
	}

	@Test
	public void testDeletePlayerWhenDoesNotExistShouldShowError() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		playerController.deletePlayer(player);
		verify(playerView).showError("No player exists with ID 1", player);
	}
}


