package com.cricketteam.app.cricketteam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

class PlayerRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private PlayerRepository repository;

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
    }

    @AfterEach
    void tearDown() {
        client.close();
    }

    @Test
    void testFindAllReturnsEmptyList() {
        assertThat(repository.findAll()).isEmpty();
    }
}
