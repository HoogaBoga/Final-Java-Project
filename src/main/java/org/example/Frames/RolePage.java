package org.example.Frames;

import org.example.Buttons.EmployeeButton;
import org.example.Buttons.ManagerButton;
import org.example.Misc.ShadowLabel;

import javax.management.relation.Role;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class RolePage extends JFrame {

    private JLabel backButton = new JLabel("Back");

    public RolePage() throws IOException {
        // Set layout


        setLayout(new BorderLayout());

        ImageIcon greeneryImage = new ImageIcon(Objects.requireNonNull(RolePage.class.getResource("/Vector.png")));

        JLabel greeneryyImage = new JLabel();

        greeneryyImage.setBounds(438, 50, greeneryImage.getIconWidth(), greeneryImage.getIconHeight());
        greeneryyImage.setIcon(greeneryImage);

        // Create panel for frame
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);

                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(RolePage.class.getResource("/Role Page.png")));
                Image image = imageIcon.getImage();
                // Draw the image
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                revalidate();
                repaint();
            }
        };

        backButton.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(14f));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(20, 328, 33, 18);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RolePage.this.dispose();
                try {
                    new FigmaToCodeApp();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (FontFormatException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(new Dimension(621, 358));

        add(backgroundPanel, BorderLayout.CENTER);

        backgroundPanel.add(greeneryyImage, BorderLayout.WEST);

        ShadowLabel Hello = new ShadowLabel("Hello!");
        Hello.setForeground(Color.WHITE);
        Hello.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(35f));
        Hello.setBounds(20, 70, 108, 50);
        backgroundPanel.add(Hello);

        ShadowLabel wru = new ShadowLabel("What are you?");
        wru.setForeground(Color.WHITE);
        wru.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(35f));
        wru.setBounds(20, 120, 275, 50);
        backgroundPanel.add(wru);

        ManagerButton managerButton = new ManagerButton(this);
        backgroundPanel.add(managerButton);

        EmployeeButton employeeButton = new EmployeeButton(this);
        backgroundPanel.add(employeeButton);

        backgroundPanel.add(backButton);

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
