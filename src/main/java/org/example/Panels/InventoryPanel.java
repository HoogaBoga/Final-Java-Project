package org.example.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//public class InventoryPanel extends JScrollPane{
//    private static String itemName;
//    private DefaultTableModel model;
//    private JTable table;
//
//
//    public void InventoryTable(String[] args) {
//        // Column Names
//        String[] columnNames = {"Items", "Meal ID", "Quantity", "Meal Price"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
//        JTable table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//        table.setFillsViewportHeight(true);
//
//        JPanel textPanel = new JPanel();
//        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
//        textPanel.setBackground(Color.WHITE);
//
//
//        JLabel nameLabel = new JLabel(itemName);
//        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        textPanel.add(nameLabel);
//
//    }
//
//        public String DisplayItem(int meal_id) {
//            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/Spyke/IdeaProjects/FinalJavaProject/Database.db");
//                 Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery("SELECT id, quantity, price, meal_id FROM Inventory")) {
//
//
//                while (rs.next()) {
//                    int id = rs.getInt("Meal ID");
//                    int quantity = rs.getInt("Quantity");
//                    int price = rs.getInt("Price");
//                    model.addRow(new Object[]{id, quantity, price});
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        };
//
//
//        // Set up Table
//
//        JFrame frame = new JFrame("Inventory");
//        frame.add(scrollPane);
//        frame.setSize(130, 150);
//        frame.setVisible(true);
//
//    };




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
