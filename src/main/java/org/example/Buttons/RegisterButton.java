package org.example.Buttons;

import org.example.Frames.FigmaToCodeApp;
import org.example.Frames.RegisterFrame;
import org.example.Frames.RolePage;
import org.example.Misc.DatabaseManager;
import org.example.Misc.RoundedBorder;
import org.example.TextFields.PasswordField;
import org.example.TextFields.RoundedPassWordField;
import org.example.TextFields.RoundedTextField;
import org.example.TextFields.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;

public class RegisterButton extends JButton implements ActionListener {
    private RoundedTextField emailField;
    private RoundedTextField usernameField;
    private RoundedPassWordField passwordField;
    private JComboBox<String> rolesBox; // Role selection combo box
    private JFrame parentFrame;
    private int borderRadius = 30;
    private int managerCode = 1108;

    public RegisterButton(RoundedTextField usernameField, RoundedPassWordField passwordField, JComboBox<String> rolesBox, JFrame parentFrame, RoundedTextField emailField) {
        super("Register");
        this.emailField = emailField;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.rolesBox = rolesBox; // Initialize role box
        this.parentFrame = parentFrame;


        // Button settings
        this.setBounds(131, 330, 162, 29);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(new RoundedBorder(borderRadius));

        // Add ActionListener
        this.addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Set color and fill rounded rectangle
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Rounded corners

        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(super.getPreferredSize().width + borderRadius * 2,
                super.getPreferredSize().height + borderRadius * 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) rolesBox.getSelectedItem();
        String email = emailField.getText().trim();


        if (username.isEmpty() || password.isEmpty() || role.equals("  Select Role")) {
            JOptionPane.showMessageDialog(parentFrame, "Please fill in all fields correctly.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (role.equals("  Manager")) {
            String managerCodeInput = JOptionPane.showInputDialog(parentFrame, "Please enter the Manager Code:");

            if (managerCodeInput == null || managerCodeInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Manager Code is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!managerCodeInput.equals(String.valueOf(managerCode))) {
                JOptionPane.showMessageDialog(parentFrame, "Incorrect Manager Code. Registration failed.", "Authorization Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        boolean registrationSuccessful = registerUser(email, username, password, role);

        if (registrationSuccessful) {
            JOptionPane.showMessageDialog(parentFrame, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Registration Failed. Please try again.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean registerUser(String username, String password, String role, String email) {
        return addUser(username, password, role, email);
    }

    private boolean addUser(String email, String username, String password, String role) {
        String insertSQL = "INSERT INTO Users (email, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, email); // Set email first
            preparedStatement.setString(2, username); // Set username second
            preparedStatement.setString(3, password); // Set password third
            preparedStatement.setString(4, role); // Set role last

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User '" + username + "' added successfully!");
                refreshUserList();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void refreshUserList() {
        // Re-fetch users from the database
        String query = "SELECT * FROM Users";
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                // Add user to in-memory list or session storage
                // e.g., userList.add(new User(username, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
