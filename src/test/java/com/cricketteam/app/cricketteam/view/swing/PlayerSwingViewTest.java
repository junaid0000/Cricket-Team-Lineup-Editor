package com.cricketteam.app.cricketteam.view.swing;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

import com.cricketteam.app.cricketteam.controller.PlayerController;
import com.cricketteam.app.cricketteam.model.Player;

public class PlayerSwingViewTest {

    private FrameFixture window;
    private PlayerSwingView playerSwingView;

    @Mock
    private PlayerController playerController;
    private AutoCloseable closeable;

    @BeforeEach
    public void onSetUp() {
        closeable = MockitoAnnotations.openMocks(this);
        FailOnThreadViolationRepaintManager.install();
        GuiActionRunner.execute(() -> {
            playerSwingView = new PlayerSwingView();
            playerSwingView.setPlayerController(playerController);
            return playerSwingView;
        });
        window = new FrameFixture(playerSwingView);
        window.show(); 
    }

    @AfterEach
    public void onTearDown() throws Exception {
        window.cleanUp();
        closeable.close();
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

    @Test
    void testWhenIdNameAndRoleAreNonEmptyThenAddButtonShouldBeEnabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Junaid");
        window.textBox("roleTextBox").enterText("Batsman");
        window.button("addButton").requireEnabled();
    }

    @Test
    void testWhenEitherIdOrNameOrRoleAreBlankThenAddButtonShouldBeDisabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Junaid");
        window.textBox("roleTextBox").enterText(" ");
        window.button("addButton").requireDisabled();
        
        window.textBox("idTextBox").setText("");
        window.textBox("nameTextBox").setText("");
        window.textBox("roleTextBox").setText("");

        window.textBox("idTextBox").enterText(" ");
        window.textBox("nameTextBox").enterText("Junaid");
        window.textBox("roleTextBox").enterText("Batsman");
        window.button("addButton").requireDisabled();

        window.textBox("idTextBox").setText("");
        window.textBox("nameTextBox").setText("");
        window.textBox("roleTextBox").setText("");

        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText(" ");
        window.textBox("roleTextBox").enterText("Batsman");
        window.button("addButton").requireDisabled();
    }

    @Test
    void testAddButtonShouldDelegateToPlayerControllerNewPlayer() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Junaid");
        window.textBox("roleTextBox").enterText("Batsman");
        window.button("addButton").click();
        verify(playerController).newPlayer(new Player("1", "Junaid", "Batsman"));
    }

    @Test
    void testDeleteButtonShouldBeEnabledOnlyWhenAPlayerIsSelected() {
        GuiActionRunner.execute(() -> playerSwingView.playerAdded(new Player("1", "Junaid", "Batsman")));
        window.list("playerList").selectItem(0);
        window.button("deleteButton").requireEnabled();
        window.list("playerList").clearSelection();
        window.button("deleteButton").requireDisabled();
    }

    @Test
    void testDeleteButtonShouldDelegateToPlayerControllerDeletePlayer() {
        Player player1 = new Player("1", "Junaid", "Batsman");
        Player player2 = new Player("2", "Babar", "Batsman");
        GuiActionRunner.execute(() -> {
            playerSwingView.playerAdded(player1);
            playerSwingView.playerAdded(player2);
        });
        window.list("playerList").selectItem(1);
        window.button("deleteButton").click();
        verify(playerController).deletePlayer(player2);
    }
}
