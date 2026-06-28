package com.cricketteam.app.cricketteam.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void testPlayerInitialization() {
        Player player = new Player("1", "Junaid Munir", "Batsman");
        assertEquals("1", player.getId());
        assertEquals("Junaid Munir", player.getName());
        assertEquals("Batsman", player.getRole());
    }
}
