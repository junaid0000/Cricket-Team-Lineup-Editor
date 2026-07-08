package com.cricketteam.app.cricketteam.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testPlayerInitialization() {
		Player player = new Player("1", "Junaid Munir", "Batsman");
		assertEquals("1", player.getId());
		assertEquals("Junaid Munir", player.getName());
		assertEquals("Batsman", player.getRole());
	}
}


