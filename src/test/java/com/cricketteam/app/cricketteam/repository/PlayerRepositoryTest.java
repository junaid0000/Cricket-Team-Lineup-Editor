package com.cricketteam.app.cricketteam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

class PlayerRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private PlayerRepository repository;
    private MongoCollection<Document> playerCollection;

    @BeforeAll
    static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterAll
    static void shutdownServer() {
        server.shutdown();
    }

    @BeforeEach
    void setup() {
        client = MongoClients.create("mongodb://" + serverAddress.getHostName() + ":" + serverAddress.getPort());
        repository = new PlayerMongoRepository(client, "cricket", "player");
        MongoDatabase database = client.getDatabase("cricket");
        database.drop();
        playerCollection = database.getCollection("player");
    }

    @AfterEach
    void tearDown() {
        client.close();
    }

    @Test
    void testFindAllReturnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void testFindAllWhenDatabaseIsNotEmpty() {
        playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
        playerCollection.insertOne(new Document().append("id", "2").append("name", "Babar Azam").append("role", "Batsman"));

        assertThat(repository.findAll()).containsExactly(
                new Player("1", "Junaid Munir", "Batsman"),
                new Player("2", "Babar Azam", "Batsman")
        );
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(repository.findById("99")).isNull();
    }

    @Test
    void testFindByIdFound() {
        playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
        assertThat(repository.findById("1")).isEqualTo(new Player("1", "Junaid Munir", "Batsman"));
    }

    @Test
    void testSave() {
        Player player = new Player("1", "Junaid Munir", "Batsman");
        repository.save(player);

        assertThat(playerCollection.find().into(new ArrayList<>()))
                .extracting(d -> new Player(d.getString("id"), d.getString("name"), d.getString("role")))
                .containsExactly(player);
    }

    @Test
    void testDelete() {
        playerCollection.insertOne(new Document().append("id", "1").append("name", "Junaid Munir").append("role", "Batsman"));
        repository.delete("1");

        assertThat(playerCollection.find().into(new ArrayList<>())).isEmpty();
    }
}
