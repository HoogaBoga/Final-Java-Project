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
    private JLabel noUser;
    private int borderRadius = 30;



    public LoginButton(TextField usernameField, PasswordField passwordField, JFrame parentFrame) {
        super("LOGIN");
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.parentFrame = parentFrame;

        // Button settings
        this.setBounds(333, 261, 265, 28);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Readex Pro", Font.PLAIN, 14));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(new RoundedBorder(borderRadius));

        noUser = new JLabel("Username or Password does not exist!");
        noUser.setForeground(new Color(255, 0, 0));
        noUser.setFont(new Font("Readex Pro", Font.PLAIN, 13));
        noUser.setBounds(333, 295, 273, 15); // Adjust bounds as needed
        noUser.setVisible(false); // Hide initially

        // Add ActionListener
        this.addActionListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        // Set color and fill rounded rectangle
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Rounded corners

        super.paintComponent(g); // Paint the button text and icon
    }

    @Override
    public Dimension getPreferredSize() {
        // Increase the size to accommodate the border radius
        return new Dimension(super.getPreferredSize().width + borderRadius * 2,
                super.getPreferredSize().height + borderRadius * 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Main.authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
            try {
                new HomeFrame();
                parentFrame.dispose();
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            noUser.setVisible(true);
        }
    }

    public JLabel getNoUser(){
        return noUser;
    }
}
