package org.example;

import javax.swing.*;
import java.awt.*;

public class ScrollPanel extends JScrollPane {
    public ScrollPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use vertical layout

        // Example content
        for (int i = 0; i < 10; i++) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setPreferredSize(new Dimension(480, 100));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel imageLabel = new JLabel(new ImageIcon("path_to_image_" + i + ".png")); // Add your image path here
            JLabel textLabel = new JLabel("Image " + i);

            itemPanel.add(imageLabel, BorderLayout.WEST);
            itemPanel.add(textLabel, BorderLayout.CENTER);

            panel.add(itemPanel);
        }

        setViewportView(panel); // Add panel to viewport
        setBounds(25, 50, 480, 322); // Set bounds of the scroll pane
        setBorder(BorderFactory.createEmptyBorder());
    }

}
