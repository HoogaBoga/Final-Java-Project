package org.example.Panels;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JScrollPane {
    private JPanel contentPanel;

    public InventoryPanel() {
        // Create the content panel to hold all components
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 12, 12)); // 3 columns with spacing
        contentPanel.setBackground(new Color(0xE8E8E8));

        // Add sample items to the content panel
        for (int i = 0; i < 9; i++) {
            contentPanel.add(createItemPanel("Item " + (i + 1), "₱" + (90 + i * 10), "path_to_image_" + i + ".png"));
        }

        // Set the content panel as the viewport view of the JScrollPane
        this.setViewportView(contentPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    private JPanel createItemPanel(String itemName, String itemPrice, String imagePath) {
        JPanel itemPanel = new JPanel();
        itemPanel.setPreferredSize(new Dimension(147, 191)); // Set fixed size for consistency
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)); // Rounded border

        // Image label (Placeholder for item image)
        JLabel imageLabel = new JLabel(new ImageIcon(imagePath)); // Use actual image path here
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        itemPanel.add(imageLabel, BorderLayout.NORTH);

        // Text panel for item name and price
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(nameLabel);

        JLabel priceLabel = new JLabel(itemPrice);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setForeground(new Color(0x4CAF50));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(priceLabel);

        itemPanel.add(textPanel, BorderLayout.CENTER);

        // Button panel
        JButton viewButton = new JButton("View");
        viewButton.setBackground(new Color(0x4CAF50));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(viewButton);
        itemPanel.add(buttonPanel, BorderLayout.SOUTH);

        return itemPanel;
    }
}
