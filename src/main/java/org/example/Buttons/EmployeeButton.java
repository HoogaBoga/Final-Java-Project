package org.example.Buttons;

import org.example.Frames.EmployeeFrame;
import org.example.Misc.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class EmployeeButton extends JButton implements ActionListener {

    private JFrame parentFrame;
    private int userId; // Logged-in user's ID

    public EmployeeButton(JFrame parentFrame, int userId) {
        super("Employee");
        this.parentFrame = parentFrame;
        this.userId = userId; // Save the logged-in user's ID

        // Button settings
        this.setBounds(30, 247, 251, 28);
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
            if(!isEmployee(userId)){
                JOptionPane.showMessageDialog(parentFrame, "You are not an Employee! Please select manager.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new EmployeeFrame(userId); // Pass userId to EmployeeFrame
            parentFrame.dispose();
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean isEmployee(int userId){
        String query = "SELECT role FROM Users WHERE id = ?";

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String role = resultSet.getString("role").trim();
                System.out.println("DEBUG: Role retrieved from database for userId " + userId + " is: " + role);
                return "Employee".equalsIgnoreCase(role);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
