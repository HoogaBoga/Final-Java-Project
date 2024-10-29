package org.example;

import javax.swing.*;
import java.awt.*;

public class ScrollPanel extends JScrollPane {
    public ScrollPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // Populate with sample items
        for (int i = 0; i < 10; i++) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setPreferredSize(new Dimension(450, 100)); // Adjust the size as needed
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            itemPanel.setBackground(Color.WHITE);

            // Sample image label (replace with actual image paths if available)
            JLabel imageLabel = new JLabel(new ImageIcon("path_to_image_" + i + ".png")); // Use placeholder image if no actual images
            JLabel textLabel = new JLabel("Item " + i);
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            textLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JButton viewButton = new JButton("View");
            viewButton.setBackground(new Color(34, 139, 34)); // Green background
            viewButton.setForeground(Color.WHITE);

            // Arrange components in itemPanel
            itemPanel.add(imageLabel, BorderLayout.WEST);
            itemPanel.add(textLabel, BorderLayout.CENTER);
            itemPanel.add(viewButton, BorderLayout.EAST);

            contentPanel.add(itemPanel);
            contentPanel.add(Box.createVerticalStrut(10)); // Spacing between items
        }

        setViewportView(contentPanel);
        setPreferredSize(new Dimension(480, 322));
        setBorder(BorderFactory.createEmptyBorder());
    }
}
