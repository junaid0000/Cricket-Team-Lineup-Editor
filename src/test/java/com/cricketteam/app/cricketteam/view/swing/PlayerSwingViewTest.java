package com.cricketteam.app.cricketteam.view.swing;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

	@Before
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

	@After
	public void onTearDown() throws Exception {
		window.cleanUp();
		closeable.close();
	}

	@Test
	public void testControlsInitialStates() {
		org.assertj.core.api.Assertions.assertThat(playerSwingView.idTextBox.isEnabled()).isTrue();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.nameTextBox.isEnabled()).isTrue();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.roleTextBox.isEnabled()).isTrue();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.addButton.isEnabled()).isFalse();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.deleteButton.isEnabled()).isFalse();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testShowAllPlayersShouldAddPlayerDescriptionsToTheList() {
		com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
		com.cricketteam.app.cricketteam.model.Player player2 = new com.cricketteam.app.cricketteam.model.Player("2", "Babar Azam", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.showAllPlayers(java.util.Arrays.asList(player1, player2)));
		java.util.List<Player> listContents = new java.util.ArrayList<>();
		for (int i = 0; i < playerSwingView.listModel.size(); i++) {
			listContents.add(playerSwingView.listModel.getElementAt(i));
		}
		org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player1, player2);
	}

	@Test
	public void testPlayerAddedShouldAddThePlayerToTheListAndResetTheErrorLabel() {
		com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(player1));
		java.util.List<Player> listContents = new java.util.ArrayList<>();
		for (int i = 0; i < playerSwingView.listModel.size(); i++) {
			listContents.add(playerSwingView.listModel.getElementAt(i));
		}
		org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player1);
		org.assertj.core.api.Assertions.assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testPlayerRemovedShouldRemoveThePlayerFromTheListAndResetTheErrorLabel() {
		com.cricketteam.app.cricketteam.model.Player player1 = new com.cricketteam.app.cricketteam.model.Player("1", "Junaid Munir", "Batsman");
		com.cricketteam.app.cricketteam.model.Player player2 = new com.cricketteam.app.cricketteam.model.Player("2", "Babar Azam", "Batsman");
		GuiActionRunner.execute(() -> {
			playerSwingView.playerAdded(player1);
			playerSwingView.playerAdded(player2);
		});
		
		GuiActionRunner.execute(() -> playerSwingView.playerRemoved(player1));
		
		java.util.List<Player> listContents = new java.util.ArrayList<>();
		for (int i = 0; i < playerSwingView.listModel.size(); i++) {
			listContents.add(playerSwingView.listModel.getElementAt(i));
		}
		org.assertj.core.api.Assertions.assertThat(listContents).containsExactly(player2);
		org.assertj.core.api.Assertions.assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testWhenIdNameAndRoleAreNonEmptyThenAddButtonShouldBeEnabled() {
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		org.assertj.core.api.Assertions.assertThat(playerSwingView.addButton.isEnabled()).isTrue();
	}

	@Test
	public void testWhenEitherIdOrNameOrRoleAreBlankThenAddButtonShouldBeDisabled() {
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText(" ");
		});
		org.assertj.core.api.Assertions.assertThat(playerSwingView.addButton.isEnabled()).isFalse();
		
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("");
			playerSwingView.nameTextBox.setText("");
			playerSwingView.roleTextBox.setText("");
		});

		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText(" ");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		org.assertj.core.api.Assertions.assertThat(playerSwingView.addButton.isEnabled()).isFalse();

		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("");
			playerSwingView.nameTextBox.setText("");
			playerSwingView.roleTextBox.setText("");
		});

		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText(" ");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		org.assertj.core.api.Assertions.assertThat(playerSwingView.addButton.isEnabled()).isFalse();
	}

	@Test
	public void testAddButtonShouldDelegateToPlayerControllerNewPlayer() {
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
			playerSwingView.addButton.doClick();
		});
		verify(playerController).newPlayer(new Player("1", "Junaid", "Batsman"));
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAPlayerIsSelected() {
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(new Player("1", "Junaid", "Batsman")));
		GuiActionRunner.execute(() -> playerSwingView.playerList.setSelectedIndex(0));
		org.assertj.core.api.Assertions.assertThat(playerSwingView.deleteButton.isEnabled()).isTrue();
		GuiActionRunner.execute(() -> playerSwingView.playerList.clearSelection());
		org.assertj.core.api.Assertions.assertThat(playerSwingView.deleteButton.isEnabled()).isFalse();
	}

	@Test
	public void testDeleteButtonShouldDelegateToPlayerControllerDeletePlayer() {
		Player player1 = new Player("1", "Junaid", "Batsman");
		Player player2 = new Player("2", "Babar", "Batsman");
		GuiActionRunner.execute(() -> {
			playerSwingView.playerAdded(player1);
			playerSwingView.playerAdded(player2);
			playerSwingView.playerList.setSelectedIndex(1);
			playerSwingView.deleteButton.doClick();
		});
		verify(playerController).deletePlayer(player2);
	}

	@Test
	public void testWhenPlayerIsSelectedThenTextboxesShouldBePopulatedAndIdShouldBeDisabled() {
		Player player = new Player("1", "Junaid", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(player));
		GuiActionRunner.execute(() -> playerSwingView.playerList.setSelectedIndex(0));
		org.assertj.core.api.Assertions.assertThat(playerSwingView.idTextBox.getText()).isEqualTo("1");
		org.assertj.core.api.Assertions.assertThat(playerSwingView.idTextBox.isEnabled()).isFalse();
		org.assertj.core.api.Assertions.assertThat(playerSwingView.nameTextBox.getText()).isEqualTo("Junaid");
		org.assertj.core.api.Assertions.assertThat(playerSwingView.roleTextBox.getText()).isEqualTo("Batsman");
	}

	@Test
	public void testUpdateButtonShouldBeEnabledOnlyWhenAPlayerIsSelected() {
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(new Player("1", "Junaid", "Batsman")));
		GuiActionRunner.execute(() -> playerSwingView.playerList.setSelectedIndex(0));
		org.assertj.core.api.Assertions.assertThat(playerSwingView.updateButton.isEnabled()).isTrue();
		GuiActionRunner.execute(() -> playerSwingView.playerList.clearSelection());
		org.assertj.core.api.Assertions.assertThat(playerSwingView.updateButton.isEnabled()).isFalse();
	}

	@Test
	public void testUpdateButtonShouldDelegateToPlayerControllerUpdatePlayer() {
		Player player = new Player("1", "Junaid", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(player));
		GuiActionRunner.execute(() -> {
			playerSwingView.playerList.setSelectedIndex(0);
			playerSwingView.nameTextBox.setText("Junaid Munir");
			playerSwingView.roleTextBox.setText("Captain");
			playerSwingView.updateButton.doClick();
		});
		verify(playerController).updatePlayer(new Player("1", "Junaid Munir", "Captain"));
	}

	@Test
	public void testShowErrorShouldShowFormattedErrorMessageInLabel() {
		Player player = new Player("1", "Junaid", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.showError("Already exists with ID 1", player));
		org.assertj.core.api.Assertions.assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo("Error: Already exists with ID 1");
	}
}


