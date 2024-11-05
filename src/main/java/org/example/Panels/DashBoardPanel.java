package org.example.Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;

public class DashBoardPanel extends JScrollPane {
    private JPanel contentPanel;

    private static final String DB_URL = "jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db";

    public DashBoardPanel() {
        // Create the content panel to hold all components
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 12, 12)); // 3 columns with spacing
        contentPanel.setBackground(new Color(0xE8E8E8));

        // Add sample items to the content panel
        for (int i = 0; i < 9; i++) {
            contentPanel.add(createItemPanel(i + 1,"Item " + (i + 1), "₱" + (90 + i * 10), "path_to_image_" + i + ".png"));
        }

        // Set the content panel as the viewport view of the JScrollPane
        this.setViewportView(contentPanel);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    public void displayImage(int meal_id, JLabel imageLabel){
        String query = "SELECT image FROM Meals WHERE meal_id = ?";

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, meal_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                byte[] imageBytes = resultSet.getBytes("image");

                if(imageBytes != null){

                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    ImageIcon images = new ImageIcon(img.getScaledInstance(147, 191, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(images);
                    imageLabel.setText("");

                }
            }
        } catch (SQLException | IOException e){
                e.printStackTrace();
        }
    }

    private JPanel createItemPanel(int mealID, String itemName, String itemPrice, String imagePath) {
        JPanel itemPanel = new JPanel();
        itemPanel.setPreferredSize(new Dimension(147, 191)); // Set fixed size for consistency
        itemPanel.setLayout(new BorderLayout());

        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true)); // Rounded border

        // Image label (Placeholder for item image)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        itemPanel.add(imageLabel, BorderLayout.NORTH);

        displayImage(mealID, imageLabel);

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
