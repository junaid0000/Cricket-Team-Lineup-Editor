package com.cricketteam.app.cricketteam.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

public class PlayerMongoRepository implements PlayerRepository {

    private MongoCollection<Document> playerCollection;

    public PlayerMongoRepository(MongoClient client, String databaseName, String collectionName) {
        this.playerCollection = client.getDatabase(databaseName).getCollection(collectionName);
    }

    @Override
    public List<Player> findAll() {
        return StreamSupport.stream(playerCollection.find().spliterator(), false)
                .map(this::fromDocument)
                .collect(Collectors.toList());
    }

    @Override
    public Player findById(String id) {
        // Will implement in next TDD phase
        return null;
    }

    @Override
    public void save(Player player) {
        // Will implement in next TDD phase
    }

    @Override
    public void delete(String id) {
        // Will implement in next TDD phase
    }

    private Player fromDocument(Document d) {
        return new Player(d.getString("id"), d.getString("name"), d.getString("role"));
    }
}
