package org.example;

import javax.swing.*;
import java.awt.*;

public class AddMealFrame extends JFrame {

    public AddMealFrame() {
        JPanel panel = new JPanel(new GridLayout(0, 2)); // Organize in a grid
        JButton addButton = new AddMeal();
        // Add labels and text fields with a preferred size
        JTextField mealIDField = new JTextField();
        mealIDField.setPreferredSize(new Dimension(100, 20)); // Set width and height

        JTextField mealNameField = new JTextField();
        mealNameField.setPreferredSize(new Dimension(100, 20));

        JTextField mealCategoryField = new JTextField();
        mealCategoryField.setPreferredSize(new Dimension(100, 20));

        JTextField servingSizeField = new JTextField();
        servingSizeField.setPreferredSize(new Dimension(100, 20));

        JTextField mealTypeField = new JTextField();
        mealTypeField.setPreferredSize(new Dimension(100, 20));

        JTextField nutritionalValueField = new JTextField();
        nutritionalValueField.setPreferredSize(new Dimension(100, 20));

        JTextField spiceLevelField = new JTextField();
        spiceLevelField.setPreferredSize(new Dimension(100, 20));

        JTextField mealPriceField = new JTextField();
        mealPriceField.setPreferredSize(new Dimension(100, 20));

        JTextField ingredientField = new JTextField();
        ingredientField.setPreferredSize(new Dimension(100, 20));

        // Add them to the panel
        panel.add(new JLabel("Meal ID: "));
        panel.add(mealIDField);
        panel.add(new JLabel("Meal Name: "));
        panel.add(mealNameField);
        panel.add(new JLabel("Meal Category: "));
        panel.add(mealCategoryField);
        panel.add(new JLabel("Serving Size: "));
        panel.add(servingSizeField);
        panel.add(new JLabel("Meal Type: "));
        panel.add(mealTypeField);
        panel.add(new JLabel("Nutritional Value: "));
        panel.add(nutritionalValueField);
        panel.add(new JLabel("Spice Level: "));
        panel.add(spiceLevelField);
        panel.add(new JLabel("Meal Price: "));
        panel.add(mealPriceField);
        panel.add(new JLabel("Ingredient: "));
        panel.add(ingredientField);

        panel.add(addButton);


        this.setTitle("Meal Table");
        this.add(panel, BorderLayout.CENTER); // Add panel to the center
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

}
