package com.cricketteam.app.cricketteam.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import com.cricketteam.app.cricketteam.model.Player;

class PlayerRepositoryTest {

    @Test
    void testFindAllReturnsEmptyList() {
        PlayerRepository repository = null;
        List<Player> players = repository.findAll();
        assertTrue(players.isEmpty());
    }
}
