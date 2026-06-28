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
}
