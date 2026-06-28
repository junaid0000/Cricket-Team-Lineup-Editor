package com.cricketteam.app.cricketteam.view.swing;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerSwingViewTest {

    private FrameFixture window;
    private PlayerSwingView playerSwingView;

    @BeforeEach
    public void onSetUp() {
        FailOnThreadViolationRepaintManager.install();
        GuiActionRunner.execute(() -> {
            playerSwingView = new PlayerSwingView();
            return playerSwingView;
        });
        window = new FrameFixture(playerSwingView);
        window.show(); 
    }

    @AfterEach
    public void onTearDown() {
        window.cleanUp();
    }

    @Test
    void testControlsInitialStates() {
        window.label("idLabel").requireText("id");
        window.textBox("idTextBox").requireEnabled();
        window.label("nameLabel").requireText("name");
        window.textBox("nameTextBox").requireEnabled();
        window.label("roleLabel").requireText("role");
        window.textBox("roleTextBox").requireEnabled();
        window.button("addButton").requireDisabled();
        window.button("deleteButton").requireDisabled();
        window.list("playerList");
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    void testShowAllPlayersShouldAddPlayerDescriptionsToTheList() {
        com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
        com.cricketteam.app.cricketteam.model.Player player2 = new com.cricketteam.app.cricketteam.model.Player("2", "Babar Azam", "Batsman");
        GuiActionRunner.execute(() -> playerSwingView.showAllPlayers(java.util.Arrays.asList(player1, player2)));
        String[] listContents = window.list().contents();
        org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player1.toString(), player2.toString());
    }

    @Test
    void testPlayerAddedShouldAddThePlayerToTheListAndResetTheErrorLabel() {
        com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
        GuiActionRunner.execute(() -> playerSwingView.playerAdded(player1));
        String[] listContents = window.list().contents();
        org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player1.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    void testPlayerRemovedShouldRemoveThePlayerFromTheListAndResetTheErrorLabel() {
        com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
        com.cricketteam.app.cricketteam.model.Player player2 = new com.cricketteam.app.cricketteam.model.Player("2", "Babar Azam", "Batsman");
        GuiActionRunner.execute(() -> {
            playerSwingView.playerAdded(player1);
            playerSwingView.playerAdded(player2);
        });
        
        GuiActionRunner.execute(() -> playerSwingView.playerRemoved(player1));
        
        String[] listContents = window.list().contents();
        org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player2.toString());
        window.label("errorMessageLabel").requireText(" ");
    }
}
