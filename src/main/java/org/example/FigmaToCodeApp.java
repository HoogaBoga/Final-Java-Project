package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FigmaToCodeApp extends JFrame {

    //TODO CREATE REGISTER BUTTON WITH FUNCTIONALITY TO ADD NEW USER
    //TODO CREATE SECOND FRAME
    public static final Font READEX_PRO_FONT = loadCustomFont();

    public static Font loadCustomFont() {
        try {
            // Load Readex Pro font from Resources folder
            File fontFile = new File("Resources/ReadexPro-Regular.ttf");
            Font readexPro = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(readexPro);
            return readexPro;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
        }
    }

    public FigmaToCodeApp() throws IOException, FontFormatException {
        // Set layout
        setLayout(new BorderLayout());

        ImageIcon greeneryImage = new ImageIcon("Resources/Vector.png");

        JLabel greeneryyImage = new JLabel();

        Font readexPro = loadCustomFont();

        greeneryyImage.setBounds(50, 50, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryyImage.setIcon(greeneryImage);

        // Create panel for frame
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon("Resources/Login Page.png");
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };


        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(new Dimension(621, 358));

        // Add welcome text
        ShadowLabel welcomeLabel = new ShadowLabel("WELCOME!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(readexPro.deriveFont(35f));
        welcomeLabel.setBounds(402, 70, 200, 40);
        backgroundPanel.add(welcomeLabel);

        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(readexPro.deriveFont(14f));
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
        usernameField.setFont(readexPro.deriveFont(14f));
        usernameField.setOpaque(true); // Ensure it's opaque
        usernameField.setMargin(new Insets(0,10,0,0));
        backgroundPanel.add(usernameField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(readexPro.deriveFont(14f));
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
        forgotPasswordLabel.setFont(readexPro.deriveFont(12f));
        forgotPasswordLabel.setBounds(505, 315, 100, 20);
        backgroundPanel.add(forgotPasswordLabel);

        JLabel registerNewUser = new JLabel("Not a User? Register Here");
        registerNewUser.setForeground(new Color(192, 191, 191));
        registerNewUser.setFont(readexPro.deriveFont(12f));
        registerNewUser.setBounds(335, 315, 200, 20);
        backgroundPanel.add(registerNewUser);

        backgroundPanel.add(greeneryyImage, BorderLayout.EAST);

        // Add login button

        LoginButton loginButton = new LoginButton(usernameField, passwordField, this);
        backgroundPanel.add(loginButton);

        backgroundPanel.add(loginButton.getNoUser());

        add(backgroundPanel, BorderLayout.CENTER);

        setTitle("Restaurant Management System");
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setIconImage(greeneryImage.getImage());

        // Pack frame
        pack();

        // Center frame
        setLocationRelativeTo(null);

        // Set visible
        setVisible(true);

        setResizable(false);
    }



}

