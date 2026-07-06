package com.cricketteam.app.cricketteam.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cricketteam.app.cricketteam.controller.PlayerController;
import com.cricketteam.app.cricketteam.repository.PlayerMongoRepository;
import com.cricketteam.app.cricketteam.view.swing.PlayerSwingView;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class CricketTeamSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "cricket";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "player";

	public static void main(String[] args) {
		new CommandLine(new CricketTeamSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = MongoClients.create("mongodb://" + mongoHost + ":" + mongoPort);
				PlayerMongoRepository playerRepository =
					new PlayerMongoRepository(mongoClient, databaseName, collectionName);
				PlayerSwingView playerSwingView = new PlayerSwingView();
				PlayerController playerController =
					new PlayerController(playerSwingView, playerRepository);
				playerSwingView.setPlayerController(playerController);
				playerSwingView.setVisible(true);
				
				new Thread(() -> {
					try {
						playerController.allPlayers();
					} catch (Exception e) {
						System.err.println("Could not connect to MongoDB. You can still view the UI!");
						Logger.getLogger(getClass().getName())
							.log(Level.SEVERE, "Exception", e);
					}
				}).start();
			} catch (Exception e) {
				Logger.getLogger(getClass().getName())
					.log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}
}
