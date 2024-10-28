package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class RolePage extends JFrame {

    public RolePage() throws IOException {
        // Set layout
        setLayout(new BorderLayout());

        ImageIcon greeneryImage = new ImageIcon("Resources/Vector.png");

        JLabel greeneryyImage = new JLabel();

        greeneryyImage.setBounds(438, 50, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryyImage.setIcon(greeneryImage);

        // Create panel for frame
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon("Resources/Role Page.png");
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };

        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(new Dimension(621, 358));

        add(backgroundPanel, BorderLayout.CENTER);

        backgroundPanel.add(greeneryyImage, BorderLayout.WEST);

        ShadowLabel Hello = new ShadowLabel("Hello!");
        Hello.setForeground(Color.WHITE);
        Hello.setFont(new Font("Readex Pro", Font.PLAIN, 35));
        Hello.setBounds(6, 70, 108, 50);
        backgroundPanel.add(Hello);

        ShadowLabel wru = new ShadowLabel("What are you?");
        wru.setForeground(Color.WHITE);
        wru.setFont(new Font("Readex Pro", Font.PLAIN, 35));
        wru.setBounds(6, 120, 275, 50);
        backgroundPanel.add(wru);

        ManagerButton managerButton = new ManagerButton(this);
        backgroundPanel.add(managerButton);

        EmployeeButton employeeButton = new EmployeeButton(this);
        backgroundPanel.add(employeeButton);

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

    public static void main(String[] args) throws SQLException, IOException{
        new RolePage();
    }
}
