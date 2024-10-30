package org.example.Panels;

import javax.swing.*;
import java.awt.*;

public class PromotionsPanel extends JScrollPane{
    private JPanel contentPanel;

    public PromotionsPanel() {
        // Create the content panel to hold all components
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // Add sample content
        JLabel titleLabel = new JLabel("Dashboard Content Here");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);

        contentPanel.add(titleLabel);

        // Add multiple components to fill up the panel for testing scrolling
        for (int i = 1; i <= 20; i++) {
            JLabel label = new JLabel("Item " + i);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            contentPanel.add(label);
        }

        // Set the content panel as the viewport view of the JScrollPane
        this.setViewportView(contentPanel);

        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
}
