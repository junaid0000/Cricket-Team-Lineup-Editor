package com.cricketteam.app.cricketteam.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cricketteam.app.cricketteam.model.Player;
import com.cricketteam.app.cricketteam.repository.PlayerRepository;
import com.cricketteam.app.cricketteam.view.PlayerView;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerView playerView;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void testAllPlayers() {
        List<Player> players = Arrays.asList(new Player("1", "Junaid Munir", "Batsman"));
        when(playerRepository.findAll()).thenReturn(players);

        playerController.allPlayers();

        verify(playerView).showAllPlayers(players);
    }
}
