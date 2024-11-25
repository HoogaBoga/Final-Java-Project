package org.example.Buttons;

import org.example.Frames.HomeFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class ManagerButton extends JButton implements ActionListener {

    private JFrame parentFrame;
    private int userId; // Logged-in user's ID
    private int managerCode = 1108;
    private static final String DB_URL = "jdbc:sqlite:" + ManagerButton.class.getResource("/Database.db").getPath();

    public ManagerButton(JFrame parentFrame, int userId) {
        super("Manager");
        this.parentFrame = parentFrame;
        this.userId = userId; // Save the logged-in user's ID

        // Button settings
        this.setBounds(30, 197, 251, 28);
        this.setBackground(new Color(0xFFFFFF));
        this.setForeground(new Color(0x987284));
        this.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        this.setFocusable(false);

        // Add ActionListener
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("DEBUG: Checking role for userId = " + userId);

            if (!isManagerRole(userId)) {
                JOptionPane.showMessageDialog(parentFrame, "You are not a Manager! Please select employee.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String manager = JOptionPane.showInputDialog("Please enter the manager code: ");

            if (manager == null || manager.trim().isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Manager Code is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!manager.equals(String.valueOf(managerCode))) {
                JOptionPane.showMessageDialog(parentFrame, "Incorrect Manager Code. Access denied.", "Authorization Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new HomeFrame(userId); // Pass userId to HomeFrame
            parentFrame.dispose();

        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }


    }

    private boolean isManagerRole(int userId) {
        String query = "SELECT role FROM Users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String role = resultSet.getString("role").trim();
                System.out.println("DEBUG: Role retrieved from database for userId " + userId + " is: " + role);
                return "Manager".equalsIgnoreCase(role); // Check if the role is "Manager"
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if the role is not found or an error occurs
    }
}
