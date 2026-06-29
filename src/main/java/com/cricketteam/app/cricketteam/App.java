package com.cricketteam.app.cricketteam;

import java.awt.EventQueue;

import com.cricketteam.app.cricketteam.controller.PlayerController;
import com.cricketteam.app.cricketteam.repository.PlayerMongoRepository;
import com.cricketteam.app.cricketteam.view.swing.PlayerSwingView;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PlayerSwingView playerSwingView = new PlayerSwingView();
                playerSwingView.setVisible(true);

                new Thread(() -> {
                    try {
                        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
                        PlayerMongoRepository playerRepository = new PlayerMongoRepository(mongoClient, "cricket", "player");
                        PlayerController playerController = new PlayerController(playerSwingView, playerRepository);  
                        playerSwingView.setPlayerController(playerController);
                        
                        playerController.allPlayers();
                    } catch (Exception e) {
                        System.err.println("Could not connect to MongoDB. You can still view the UI!");
                    }
                }).start();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
