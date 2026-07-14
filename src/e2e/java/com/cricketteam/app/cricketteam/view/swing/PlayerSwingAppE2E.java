package com.cricketteam.app.cricketteam.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.cricketteam.app.cricketteam.model.Player;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

/**
 * To run this test from Eclipse, a Docker daemon/Docker Desktop must be running on the host machine.
 * Testcontainers will automatically spin up the required MongoDB container.
 */
@RunWith(GUITestRunner.class)
public class PlayerSwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

	private static final String CRICKET_DB_NAME = "cricket";
	private static final String PLAYER_COLLECTION_NAME = "player";

	private FrameFixture window;
	private MongoClient client;
	private MongoCollection<Document> playerCollection;

	@Override
	protected void onSetUp() {
		client = MongoClients.create(mongo.getReplicaSetUrl());
		client.getDatabase(CRICKET_DB_NAME).drop();
		playerCollection = client
			.getDatabase(CRICKET_DB_NAME)
			.getCollection(PLAYER_COLLECTION_NAME);
	}

	private void startApp() {
		application("com.cricketteam.app.cricketteam.app.swing.CricketTeamSwingApp")
			.withArgs(
				"--mongo-host=" + mongo.getHost(),
				"--mongo-port=" + mongo.getFirstMappedPort(),
				"--db-name=" + CRICKET_DB_NAME,
				"--db-collection=" + PLAYER_COLLECTION_NAME)
			.start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Cricket Team Lineup Editor".equals(frame.getTitle()) && frame.isShowing();
			}
		}).withTimeout(20000).using(robot());
	}

	@After
	public void closeMongoClient() {
		client.close();
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		startApp();
		fillPlayerFields("1", "Junaid Munir", "Batsman");
		PlayerSwingView view = (PlayerSwingView) window.target();
		GuiActionRunner.execute(() -> view.addButton.doClick());
		assertListContainsExactly(new String[] { "Player [id=1, name=Junaid Munir, role=Batsman]" });
		assertThat(findPlayerById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Player("1", "Junaid Munir", "Batsman"));
	}

	@Test
	@GUITest
	public void testUpdateButtonSuccess() {
		addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
		startApp();
		// wait for the UI to show the player
		window.list("playerList").requireItemCount(1);

		PlayerSwingView view = (PlayerSwingView) window.target();
		GuiActionRunner.execute(() -> {
			view.playerList.setSelectedIndex(0);
			view.nameTextBox.setText("Junaid M");
			view.roleTextBox.setText("Captain");
			view.updateButton.doClick();
		});
		
		assertListContainsExactly(new String[] { "Player [id=1, name=Junaid M, role=Captain]" });
		assertThat(findPlayerById("1"))
			.usingRecursiveComparison()
			.isEqualTo(new Player("1", "Junaid M", "Captain"));
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		addTestPlayerToDatabase("1", "Junaid Munir", "Batsman");
		startApp();
		window.list("playerList").requireItemCount(1);

		PlayerSwingView view = (PlayerSwingView) window.target();
		GuiActionRunner.execute(() -> {
			view.playerList.setSelectedIndex(0);
			view.deleteButton.doClick();
		});
		assertListContainsExactly(new String[] {});
		assertThat(findPlayerById("1"))
			.isNull();
	}

	private void fillPlayerFields(String id, String name, String role) {
		PlayerSwingView view = (PlayerSwingView) window.target();
		GuiActionRunner.execute(() -> {
			view.idTextBox.setText(id);
			view.nameTextBox.setText(name);
			view.roleTextBox.setText(role);
		});
	}

	private void assertListContainsExactly(String[] expectedContents) {
		assertThat(window.list("playerList").contents())
			.containsExactly(expectedContents);
	}

	private void addTestPlayerToDatabase(String id, String name, String role) {
		playerCollection.insertOne(
			new Document()
				.append("id", id)
				.append("name", name)
				.append("role", role));
	}

	private Player findPlayerById(String id) {
		Document playerDocument = playerCollection.find(Filters.eq("id", id)).first();
		if (playerDocument == null) {
			return null;
		}
		return new Player(
			"" + playerDocument.get("id"),
			"" + playerDocument.get("name"),
			"" + playerDocument.get("role"));
	}
}


