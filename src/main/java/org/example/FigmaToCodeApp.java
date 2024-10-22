package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class FigmaToCodeApp extends JFrame {


    public FigmaToCodeApp() throws IOException {
        // Set layout
        setLayout(new BorderLayout());

        JLabel error = new JLabel("Wrong UserName or Password");
        JLabel correct = new JLabel("Login Successful!");

        JFrame frame2 = new JFrame();
        JFrame frame3 = new JFrame();
        JFrame frame = new JFrame();
        // Create panel for frame
        JPanel framePanel = new JPanel();
        framePanel.setLayout(null);
        framePanel.setBackground(new Color(0, 163, 108));
        framePanel.setPreferredSize(new Dimension(621, 358));

        frame2.setSize(500, 200);
        frame2.setLayout(new GridBagLayout());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setResizable(false);
        frame2.setLocationRelativeTo(null);
        frame2.add(error);


        frame3.setSize(500, 200);
        frame3.setLayout(new GridBagLayout());
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setResizable(false);
        frame3.setLocationRelativeTo(null);
        frame3.add(correct);


        // Add welcome text
        JLabel welcomeLabel = new JLabel("WELCOME!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Readex Pro", Font.PLAIN, 35));
        welcomeLabel.setBounds(380, 70, 200, 40);
        framePanel.add(welcomeLabel);

        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        usernameLabel.setBounds(329, 135, 100, 20);
        framePanel.add(usernameLabel);

        // Add username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(329, 153, 273, 28);
        usernameField.setBackground(new Color(251, 250, 242));
        usernameField.setBorder(BorderFactory.createEmptyBorder());
        framePanel.add(usernameField);

        // Add password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        passwordLabel.setBounds(329, 200, 100, 20);
        framePanel.add(passwordLabel);

        // Add password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(329, 218, 273, 28);
        passwordField.setBackground(new Color(251, 250, 242));
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        framePanel.add(passwordField);


        // Add forgot password label
        JLabel forgotPasswordLabel = new JLabel("Forgot Password");
        forgotPasswordLabel.setForeground(new Color(192, 191, 191));
        forgotPasswordLabel.setFont(new Font("Readex Pro", Font.PLAIN, 12));
        forgotPasswordLabel.setBounds(505, 312, 100, 20);
        framePanel.add(forgotPasswordLabel);

        // Add login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(329, 261, 273, 28);
        loginButton.setBackground(new Color(152, 130, 132));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.authenticateUser(usernameField.getText(), passwordField.getText())) {
                    try {
                        new Newframe();
                        dispose();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                } else
                    frame2.setVisible(true);

            }
        });
        framePanel.add(loginButton);

        add(framePanel, BorderLayout.CENTER);

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pack frame
        pack();

        // Center frame
        setLocationRelativeTo(null);

        // Set visible
        setVisible(true);
    }
}

