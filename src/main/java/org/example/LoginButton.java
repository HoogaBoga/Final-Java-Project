package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class LoginButton extends JButton implements ActionListener {
    private TextField usernameField;
    private PasswordField passwordField;
    private JFrame parentFrame;



    public LoginButton(TextField usernameField, PasswordField passwordField, JFrame parentFrame) {
        super("LOGIN");
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.parentFrame = parentFrame;

        // Button settings
        this.setBounds(329, 261, 273, 28);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        this.setFocusable(false);

        // Add ActionListener
        this.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (Main.authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
            try {
                JOptionPane.showMessageDialog(parentFrame, "Login Successful!", "Success", JOptionPane.PLAIN_MESSAGE);
                new Newframe();
                parentFrame.dispose();
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Wrong Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            usernameField.setText("");
            passwordField.setText("");
        }
    }
}
