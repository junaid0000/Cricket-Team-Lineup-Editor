package com.cricketteam.app.cricketteam.repository;

import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class PlayerMongoRepository implements PlayerRepository {

	private final MongoCollection<Document> playerCollection;

	public PlayerMongoRepository(MongoClient client, String databaseName, String collectionName) {
		this.playerCollection = client.getDatabase(databaseName).getCollection(collectionName);
	}

	@Override
	public List<Player> findAll() {
		return StreamSupport.stream(playerCollection.find().spliterator(), false)
				.map(this::fromDocument)
				.toList();
	}

	@Override
	public Player findById(String id) {
		Document d = playerCollection.find(Filters.eq("id", id)).first();
		if (d != null) {
			return fromDocument(d);
		}
		return null;
	}

	@Override
	public void save(Player player) {
		playerCollection.insertOne(new Document()
				.append("id", player.getId())
				.append("name", player.getName())
				.append("role", player.getRole()));
	}

	@Override
	public void update(Player player) {
		playerCollection.replaceOne(Filters.eq("id", player.getId()),
				new Document()
						.append("id", player.getId())
						.append("name", player.getName())
						.append("role", player.getRole()));
	}

	@Override
	public void delete(String id) {
		playerCollection.deleteOne(Filters.eq("id", id));
	}

	private Player fromDocument(Document d) {
		return new Player(d.getString("id"), d.getString("name"), d.getString("role"));
	}
}
