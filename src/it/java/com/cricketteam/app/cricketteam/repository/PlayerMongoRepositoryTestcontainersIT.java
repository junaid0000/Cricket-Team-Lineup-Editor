package com.cricketteam.app.cricketteam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PlayerMongoRepositoryTestcontainersIT {

    @ClassRule
    public static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    private MongoClient client;
    private PlayerMongoRepository playerRepository;
    private MongoCollection<Document> playerCollection;

    private static final String CRICKET_DB_NAME = "cricket";
    private static final String PLAYER_COLLECTION_NAME = "player";

    @Before
    public void setup() {
        client = MongoClients.create(mongo.getReplicaSetUrl());
        playerRepository = new PlayerMongoRepository(client, CRICKET_DB_NAME, PLAYER_COLLECTION_NAME);
        MongoDatabase database = client.getDatabase(CRICKET_DB_NAME);
        database.drop();
        playerCollection = database.getCollection(PLAYER_COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testSave() {
        Player player = new Player("1", "Junaid Munir", "Batsman");
        playerRepository.save(player);
        assertThat(readAllPlayersFromDatabase())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactly(player);
    }

    @Test
    public void testFindAll() {
        addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
        addTestPlayerToDatabase("2", "Babar Azam", "Captain");
        assertThat(playerRepository.findAll())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactly(
                new Player("1", "Junaid Munir", "Batsman"),
                new Player("2", "Babar Azam", "Captain"));
    }

    @Test
    public void testFindByIdWhenPlayerExists() {
        addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
        addTestPlayerToDatabase("2", "Babar Azam", "Captain");
        assertThat(playerRepository.findById("2"))
            .usingRecursiveComparison()
            .isEqualTo(new Player("2", "Babar Azam", "Captain"));
    }

    @Test
    public void testFindByIdWhenPlayerDoesNotExist() {
        addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
        assertThat(playerRepository.findById("2")).isNull();
    }

    @Test
    public void testUpdate() {
        addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
        Player editedPlayer = new Player("1", "Junaid Munir Updated", "All-Rounder");
        playerRepository.update(editedPlayer);
        assertThat(readAllPlayersFromDatabase())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactly(editedPlayer);
    }

    @Test
    public void testDelete() {
        addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
        addTestPlayerToDatabase("2", "Babar Azam", "Captain");
        playerRepository.delete("1");
        assertThat(readAllPlayersFromDatabase())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactly(new Player("2", "Babar Azam", "Captain"));
    }

    private void addTestPlayerToDatabase(String id, String name, String role) {
        playerCollection.insertOne(
            new Document()
                .append("id", id)
                .append("name", name)
                .append("role", role));
    }

    private List<Player> readAllPlayersFromDatabase() {
        return StreamSupport.stream(playerCollection.find().spliterator(), false)
            .map(d -> new Player(
                "" + d.get("id"),
                "" + d.get("name"),
                "" + d.get("role")))
            .toList();
    }
}
