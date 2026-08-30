package com.cricketteam.app.cricketteam.view.swing;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cricketteam.app.cricketteam.controller.PlayerController;
import com.cricketteam.app.cricketteam.model.Player;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GUITestRunner.class)
@GUITest
public class PlayerSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private PlayerSwingView playerSwingView;

	@Mock
	private PlayerController playerController;
	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		playerSwingView = GuiActionRunner.execute(() -> {
			PlayerSwingView view = new PlayerSwingView();
			view.setPlayerController(playerController);
			return view;
		});
		window = new FrameFixture(robot(), playerSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		if (closeable != null) {
			closeable.close();
		}
	}

	private List<Player> getListModelContents() {
		List<Player> contents = new ArrayList<>();
		for (int i = 0; i < playerSwingView.listModel.size(); i++) {
			contents.add(playerSwingView.listModel.getElementAt(i));
		}
		return contents;
	}

	@Test
	public void testControlsInitialStates() {
		assertThat(playerSwingView.idTextBox.isEnabled()).isTrue();
		assertThat(playerSwingView.nameTextBox.isEnabled()).isTrue();
		assertThat(playerSwingView.roleTextBox.isEnabled()).isTrue();
		assertThat(playerSwingView.addButton.isEnabled()).isFalse();
		assertThat(playerSwingView.deleteButton.isEnabled()).isFalse();
		assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testShowAllPlayersShouldAddPlayerDescriptionsToTheList() {
		Player player1 = new Player("1", "Junaid Munir", "Batsman");
		Player player2 = new Player("2", "Babar Azam", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.showAllPlayers(Arrays.asList(player1, player2)));
		assertThat(getListModelContents()).containsExactly(player1, player2);
	}

	@Test
	public void testPlayerAddedShouldAddThePlayerToTheListAndResetTheErrorLabel() {
		Player player1 = new Player("1", "Junaid Munir", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(player1));
		assertThat(getListModelContents()).containsExactly(player1);
		assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testPlayerRemovedShouldRemoveThePlayerFromTheListAndResetTheErrorLabel() {
		Player player1 = new Player("1", "Junaid Munir", "Batsman");
		Player player2 = new Player("2", "Babar Azam", "Batsman");
		GuiActionRunner.execute(() -> {
			playerSwingView.playerAdded(player1);
			playerSwingView.playerAdded(player2);
		});

		GuiActionRunner.execute(() -> playerSwingView.playerRemoved(player1));

		assertThat(getListModelContents()).containsExactly(player2);
		assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo(" ");
	}

	@Test
	public void testWhenIdNameAndRoleAreNonEmptyThenAddButtonShouldBeEnabled() {
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		assertThat(playerSwingView.addButton.isEnabled()).isTrue();
	}

	@Test
	public void testWhenEitherIdOrNameOrRoleAreBlankThenAddButtonShouldBeDisabled() {
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText(" ");
		});
		assertThat(playerSwingView.addButton.isEnabled()).isFalse();

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
		assertThat(playerSwingView.addButton.isEnabled()).isFalse();

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
		assertThat(playerSwingView.addButton.isEnabled()).isFalse();
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
	public void testDeleteButtonEnableLogic() {
		// initially disabled
		assertThat(playerSwingView.deleteButton.isEnabled()).isFalse();
		// fill required fields
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		assertThat(playerSwingView.deleteButton.isEnabled()).isTrue();
		// clear ID to disable again
		GuiActionRunner.execute(() -> playerSwingView.idTextBox.setText(""));
		assertThat(playerSwingView.deleteButton.isEnabled()).isFalse();
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
	public void testWhenPlayerIsSelectedThenTextboxesShouldBePopulated() {
		Player player = new Player("1", "Junaid", "Batsman");
		GuiActionRunner.execute(() -> playerSwingView.playerAdded(player));
		GuiActionRunner.execute(() -> playerSwingView.playerList.setSelectedIndex(0));
		assertThat(playerSwingView.idTextBox.getText()).isEqualTo("1");
		assertThat(playerSwingView.idTextBox.isEnabled()).isTrue();
		assertThat(playerSwingView.nameTextBox.getText()).isEqualTo("Junaid");
		assertThat(playerSwingView.roleTextBox.getText()).isEqualTo("Batsman");
	}

	@Test
	public void testUpdateButtonEnableLogic() {
		assertThat(playerSwingView.updateButton.isEnabled()).isFalse();
		GuiActionRunner.execute(() -> {
			playerSwingView.idTextBox.setText("1");
			playerSwingView.nameTextBox.setText("Junaid");
			playerSwingView.roleTextBox.setText("Batsman");
		});
		assertThat(playerSwingView.updateButton.isEnabled()).isTrue();
		GuiActionRunner.execute(() -> playerSwingView.idTextBox.setText(""));
		assertThat(playerSwingView.updateButton.isEnabled()).isFalse();
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
		assertThat(playerSwingView.errorMessageLabel.getText()).isEqualTo("Error: Already exists with ID 1");
	}
}
