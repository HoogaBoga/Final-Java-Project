package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FigmaToCodeApp extends JFrame {

    //TODO CREATE REGISTER BUTTON WITH FUNCTIONALITY TO ADD NEW USER
    //TODO CREATE SECOND FRAME

    public FigmaToCodeApp() throws IOException {
        // Set layout
        setLayout(new BorderLayout());

        ImageIcon greeneryImage = new ImageIcon("Vector.png");

        JLabel greeneryyImage = new JLabel();

        greeneryyImage.setBounds(50, 50, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryyImage.setIcon(greeneryImage);

        JFrame frame2 = new JFrame();
        // Create panel for frame
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon("Login Page.png");
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };


        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(new Dimension(621, 358));
        
        frame2.setSize(500, 200);
        frame2.setLayout(new GridBagLayout());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setResizable(false);
        frame2.setLocationRelativeTo(null);


        // Add welcome text
        ShadowLabel welcomeLabel = new ShadowLabel("WELCOME!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Readex Pro", Font.PLAIN, 35));
        welcomeLabel.setBounds(380, 70, 200, 40);
        backgroundPanel.add(welcomeLabel);

        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        usernameLabel.setBounds(329, 135, 100, 20);
        backgroundPanel.add(usernameLabel);

        // Add username field
        // Create a rounded JTextField
        // Add username field
        // Add username field
        TextField usernameField = new TextField();

        usernameField.setBounds(329, 153, 273, 42);
        usernameField.setBackground(new Color(251, 250, 242));
        usernameField.setForeground(Color.BLACK); // Text color
        usernameField.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        usernameField.setOpaque(true); // Ensure it's opaque
        usernameField.setMargin(new Insets(0,10,0,0));
        backgroundPanel.add(usernameField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        passwordLabel.setBounds(329, 200, 100, 20);
        backgroundPanel.add(passwordLabel);

        // Add password field
        PasswordField passwordField = new PasswordField();
        passwordField.setBounds(329, 218, 273, 42);
        passwordField.setBackground(new Color(251, 250, 242));
        backgroundPanel.add(passwordField);


        // Add forgot password label
        JLabel forgotPasswordLabel = new JLabel("Forgot Password");
        forgotPasswordLabel.setForeground(new Color(192, 191, 191));
        forgotPasswordLabel.setFont(new Font("Readex Pro", Font.PLAIN, 12));
        forgotPasswordLabel.setBounds(505, 312, 100, 20);
        backgroundPanel.add(forgotPasswordLabel);

        backgroundPanel.add(greeneryyImage, BorderLayout.EAST);

        // Add login button

        LoginButton loginButton = new LoginButton(usernameField, passwordField, this);
        backgroundPanel.add(loginButton);

        add(backgroundPanel, BorderLayout.CENTER);

        setTitle("Restaurant Management System");
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pack frame
        pack();

        // Center frame
        setLocationRelativeTo(null);

        // Set visible
        setVisible(true);

        setResizable(false);
    }



}

