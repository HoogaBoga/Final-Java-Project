package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Newframe extends JFrame {
    public Newframe() throws IOException {
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

        setVisible(true);

    }
}
