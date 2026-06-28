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
    private JButton deleteButton;
    private JList<Player> playerList;
    private DefaultListModel<Player> listModel;
    private JLabel errorMessageLabel;

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
        deleteButton = new JButton("Delete");
        deleteButton.setName("deleteButton");
        deleteButton.setEnabled(false);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel);

        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        playerList.setName("playerList");
        JScrollPane scrollPane = new JScrollPane(playerList);
        panel.add(scrollPane);

        errorMessageLabel = new JLabel(" ");
        errorMessageLabel.setName("errorMessageLabel");
        panel.add(errorMessageLabel);

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void showAllPlayers(List<Player> players) {
        players.forEach(listModel::addElement);
    }

    @Override
    public void playerAdded(Player player) {
        listModel.addElement(player);
        errorMessageLabel.setText(" ");
    }

    @Override
    public void playerRemoved(Player player) {
        listModel.removeElement(player);
        errorMessageLabel.setText(" ");
    }
}
