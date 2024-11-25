package org.example.Buttons;

import org.example.Frames.FigmaToCodeApp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;

public class UserNameBox extends JButton {

    private int loggedInUserId;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public UserNameBox(int userId) {
        super("Username");
        this.loggedInUserId = userId;

        ImageIcon dashBoardIcon = new ImageIcon(Objects.requireNonNull(FigmaToCodeApp.class.getResource("/UserName.png")));

        this.setBounds(410, 13, 80, 20);
        this.setBackground(new Color(255, 255, 255));
        this.setForeground(Color.BLACK);
        this.setIcon(dashBoardIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(Font.PLAIN, 8f));
        this.setFocusable(false);
        this.setFocusPainted(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        // Fetch and set the username
        setUserName();
    }

    public void setUserName() {
        String query = "SELECT username FROM Users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, loggedInUserId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                this.setText(username); // Set the button text to the username
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.setText("Error");
        }
    }
}
