package org.example.Panels;

import org.example.Frames.AddMealFrame;

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

    public void displayName(int meal_id, JLabel nameLabel){
        String query = "SELECT meal_name FROM Meals WHERE meal_id = ?";

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setInt(1, meal_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String mealName =  resultSet.getString("meal_name");

                nameLabel.setText(mealName);
            } else {
                nameLabel.setText("Meal Not Found");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private JPanel createItemPanel(int mealID, String itemName, String itemPrice, String imagePath) {
        JPanel itemPanel = new JPanel();
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setPreferredSize(new Dimension(147, 191)); // Set fixed size for consistency
        itemPanel.setLayout(null); // Use null layout for custom positioning

        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true)); // Rounded border

        // Image label (Placeholder for item image)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBounds(10, 10, 121, 91); // Correct bounds for imageLabel
        itemPanel.add(imageLabel);
        displayImage(mealID, imageLabel);

        // Create nameLabel
        JLabel nameLabel = new JLabel();
        nameLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(14f));
        nameLabel.setBounds(10, 111, 100, 15); // Set bounds for nameLabel
        itemPanel.add(nameLabel);
        displayName(mealID, nameLabel);

        // Create priceLabel
        JLabel priceLabel = new JLabel(itemPrice);
        priceLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(14f));
        priceLabel.setForeground(new Color(0x4CAF50));
        priceLabel.setBounds(10, 136, 49, 15); // Set bounds for priceLabel
        itemPanel.add(priceLabel);

        // Button for viewing
        JButton viewButton = new JButton("View");
        viewButton.setBackground(new Color(0x4CAF50));
        viewButton.setFont(AddMealFrame.INTER_FONT.deriveFont(12f));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);
        viewButton.setBounds(10, 161, 130, 15); // Set bounds for the button
        itemPanel.add(viewButton);

        // Call revalidate and repaint to ensure visibility
        itemPanel.revalidate();
        itemPanel.repaint();

        return itemPanel;
    }

}
