package com.cricketteam.app.cricketteam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class PlayerRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private PlayerRepository repository;
	private MongoCollection<Document> playerCollection;

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = MongoClients.create("mongodb://" + serverAddress.getHostName() + ":" + serverAddress.getPort());
		repository = new PlayerMongoRepository(client, "cricket", "player");
		MongoDatabase database = client.getDatabase("cricket");
		database.drop();
		playerCollection = database.getCollection("player");
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllReturnsEmptyList() {
		assertThat(repository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
		playerCollection.insertOne(new Document().append("id", "2").append("name", "Babar Azam").append("role", "Batsman"));

		assertThat(repository.findAll()).containsExactly(
				new Player("1", "Junaid Munir", "Batsman"),
				new Player("2", "Babar Azam", "Batsman")
		);
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(repository.findById("99")).isNull();
	}

	@Test
	public void testFindByIdFound() {
		playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
		assertThat(repository.findById("1")).isEqualTo(new Player("1", "Junaid Munir", "Batsman"));
	}

	@Test
	public void testSave() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		repository.save(player);

		assertThat(playerCollection.find().into(new ArrayList<>()))
				.extracting(d -> new Player(d.getString("id"), d.getString("name"), d.getString("role")))
				.containsExactly(player);
	}

	@Test
	public void testUpdate() {
		playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
		Player updated = new Player("1", "Junaid Updated", "Captain");
		repository.update(updated);

		assertThat(playerCollection.find().into(new ArrayList<>()))
				.extracting(d -> new Player(d.getString("id"), d.getString("name"), d.getString("role")))
				.containsExactly(updated);
	}

	@Test
	public void testDelete() {
		playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
		repository.delete("1");

		assertThat(playerCollection.find().into(new ArrayList<>())).isEmpty();
	}
}


