package com.cricketteam.app.cricketteam.view.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.cricketteam.app.cricketteam.model.Player;
import com.cricketteam.app.cricketteam.view.PlayerView;

public class PlayerSwingView extends JFrame implements PlayerView {

    private static final long serialVersionUID = 1L;

    private JTextField idTextBox;
    private JTextField nameTextBox;
    private JTextField roleTextBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JList<Player> playerList;
    private DefaultListModel<Player> listModel;
    private JLabel errorMessageLabel;
    
    private com.cricketteam.app.cricketteam.controller.PlayerController playerController;

    public void setPlayerController(com.cricketteam.app.cricketteam.controller.PlayerController playerController) {
        this.playerController = playerController;
    }

    public PlayerSwingView() {
        setTitle("Cricket Team Lineup Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel idPanel = new JPanel(new FlowLayout());
        JLabel idLabel = new JLabel("id");
        idLabel.setName("idLabel");
        idTextBox = new JTextField(10);
        idTextBox.setName("idTextBox");
        idPanel.add(idLabel);
        idPanel.add(idTextBox);
        panel.add(idPanel);

        JPanel namePanel = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("name");
        nameLabel.setName("nameLabel");
        nameTextBox = new JTextField(10);
        nameTextBox.setName("nameTextBox");
        namePanel.add(nameLabel);
        namePanel.add(nameTextBox);
        panel.add(namePanel);

        JPanel rolePanel = new JPanel(new FlowLayout());
        JLabel roleLabel = new JLabel("role");
        roleLabel.setName("roleLabel");
        roleTextBox = new JTextField(10);
        roleTextBox.setName("roleTextBox");
        rolePanel.add(roleLabel);
        rolePanel.add(roleTextBox);
        panel.add(rolePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        addButton.setName("addButton");
        addButton.setEnabled(false);
        updateButton = new JButton("Update");
        updateButton.setName("updateButton");
        updateButton.setEnabled(false);
        deleteButton = new JButton("Delete");
        deleteButton.setName("deleteButton");
        deleteButton.setEnabled(false);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel);

        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        playerList.setName("playerList");
        
        playerList.addListSelectionListener(e -> {
            boolean isSelected = playerList.getSelectedIndex() != -1;
            if (isSelected) {
                Player selectedPlayer = playerList.getSelectedValue();
                idTextBox.setText(selectedPlayer.getId());
                idTextBox.setEnabled(false);
                nameTextBox.setText(selectedPlayer.getName());
                roleTextBox.setText(selectedPlayer.getRole());
            } else {
                idTextBox.setEnabled(true);
            }
            updateButtonStates();
        });
        
        JScrollPane scrollPane = new JScrollPane(playerList);
        panel.add(scrollPane);

        errorMessageLabel = new JLabel(" ");
        errorMessageLabel.setName("errorMessageLabel");
        panel.add(errorMessageLabel);

        add(panel, BorderLayout.CENTER);

        java.awt.event.KeyAdapter btnEnabler = new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                updateButtonStates();
            }
        };

        idTextBox.addKeyListener(btnEnabler);
        nameTextBox.addKeyListener(btnEnabler);
        roleTextBox.addKeyListener(btnEnabler);

        addButton.addActionListener(e -> {
            playerController.newPlayer(new Player(idTextBox.getText(), nameTextBox.getText(), roleTextBox.getText()));
        });

        updateButton.addActionListener(e -> {
            playerController.updatePlayer(new Player(idTextBox.getText(), nameTextBox.getText(), roleTextBox.getText()));
        });

        deleteButton.addActionListener(e -> {
            playerController.deletePlayer(playerList.getSelectedValue());
        });
    }

    @Override
    public void showAllPlayers(List<Player> players) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            players.forEach(listModel::addElement);
        });
    }

    @Override
    public void playerAdded(Player player) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            listModel.addElement(player);
            errorMessageLabel.setText(" ");
        });
    }

    @Override
    public void playerRemoved(Player player) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            listModel.removeElement(player);
            errorMessageLabel.setText(" ");
        });
    }

    @Override
    public void playerUpdated(Player player) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.getElementAt(i).getId().equals(player.getId())) {
                    listModel.setElementAt(player, i);
                    break;
                }
            }
            errorMessageLabel.setText(" ");
        });
    }

    private void updateButtonStates() {
        boolean isSelected = playerList.getSelectedIndex() != -1;
        boolean hasText = !idTextBox.getText().trim().isEmpty() &&
                          !nameTextBox.getText().trim().isEmpty() &&
                          !roleTextBox.getText().trim().isEmpty();

        addButton.setEnabled(!isSelected && hasText);
        updateButton.setEnabled(isSelected && hasText);
        deleteButton.setEnabled(isSelected);
    }
}
