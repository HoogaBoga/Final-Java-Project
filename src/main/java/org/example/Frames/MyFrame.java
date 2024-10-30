package org.example.Frames;

import org.example.Misc.CrudMeal;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame() {
        CrudMeal meal = new CrudMeal();

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JLabel mealID = new JLabel("Meal ID");
        JLabel mealName = new JLabel("Meal Name");
        JLabel mealType = new JLabel("Meal Type (Vegetarian or Non-Vegetarian)");
        JLabel mealCategory = new JLabel("Meal Category (Breakfast, Lunch, or Dinner)");
        JLabel servingSize = new JLabel("Serving Size");
        JLabel nutritionalValue = new JLabel("Nutritional Value");
        JLabel spiceLevel = new JLabel("Spicy or Not Spicy");
        JLabel mealPrice = new JLabel("Meal Price");
        JLabel ingredients = new JLabel("Ingredient");
        JButton addMeal = new JButton("Add Meal");
        JButton listMeal = new JButton("List Meal");
        JButton deleteMeal = new JButton("Delete Meal");
        JButton editMeal = new JButton("Edit Meal");


        addMeal.setPreferredSize(new Dimension(100, 30));
        addMeal.setFocusable(false);

        listMeal.setPreferredSize(new Dimension(100, 30));
        listMeal.setFocusable(false);

        deleteMeal.setPreferredSize(new Dimension(100, 30));
        deleteMeal.setFocusable(false);

        editMeal.setPreferredSize(new Dimension(100, 30));
        editMeal.setFocusable(false);

        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();

        gbc2.anchor = GridBagConstraints.CENTER;
        gbc2.insets = new Insets(7, 3, 3, 3);

        gbc2.gridx = 0;
        gbc2.gridy = 0;
        panel2.add(addMeal, gbc2);

        gbc2.gridx = 1;
        panel2.add(editMeal, gbc2);

        gbc2.gridx = 0;
        gbc2.gridy = 1;
        panel2.add(deleteMeal, gbc2);

        gbc2.gridx = 1;
        panel2.add(listMeal, gbc2);

        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 35));

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 35));

        JTextField typeField = new JTextField();
        typeField.setPreferredSize(new Dimension(200,35));

        JTextField categoryField = new JTextField();
        categoryField.setPreferredSize(new Dimension(200, 35));

        JTextField servingField = new JTextField();
        servingField.setPreferredSize(new Dimension(200, 35));

        JTextField nutritionField = new JTextField();
        nutritionField.setPreferredSize(new Dimension(200,35));

        JTextField spiceField = new JTextField();
        spiceField.setPreferredSize(new Dimension(200, 35));

        JTextField priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(200, 35));

        JTextField ingredientField = new JTextField();
        ingredientField.setPreferredSize(new Dimension(200, 35));

        addMeal.addActionListener(e ->{
            String mealNameText = nameField.getText();
            String mealCategoryText = categoryField.getText();
            int mealServingSizeText = Integer.parseInt(servingField.getText());
            String mealTypeText = typeField.getText();
            int nutritionalValueText = Integer.parseInt(nutritionField.getText());
            String spiceText = spiceField.getText();
            double mealPriceText = Double.parseDouble(priceField.getText());
            String ingredientsText = ingredientField.getText();

            meal.addMeal(mealNameText, mealCategoryText, mealServingSizeText, mealTypeText, nutritionalValueText, spiceText, mealPriceText, ingredientsText);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        panel1.setLayout(new GridBagLayout());

        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding around components
        gbc.anchor = GridBagConstraints.WEST;  // Align labels to the left

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(mealID, gbc);

        gbc.gridy++;
        panel1.add(mealName, gbc);

        gbc.gridy++;
        panel1.add(mealType, gbc);

        gbc.gridy++;
        panel1.add(mealCategory, gbc);

        gbc.gridy++;
        panel1.add(servingSize, gbc);

        gbc.gridy++;
        panel1.add(nutritionalValue, gbc);

        gbc.gridy++;
        panel1.add(spiceLevel, gbc);

        gbc.gridy++;
        panel1.add(mealPrice, gbc);

        gbc.gridy++;
        panel1.add(ingredients, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel1.add(idField, gbc);

        gbc.gridy++;
        panel1.add(nameField, gbc);

        gbc.gridy++;
        panel1.add(typeField, gbc);

        gbc.gridy++;
        panel1.add(categoryField, gbc);

        gbc.gridy++;
        panel1.add(servingField, gbc);

        gbc.gridy++;
        panel1.add(nutritionField, gbc);

        gbc.gridy++;
        panel1.add(spiceField, gbc);

        gbc.gridy++;
        panel1.add(priceField, gbc);

        gbc.gridy++;
        panel1.add(ingredientField, gbc);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Add Meal GUI");
        this.add(panel1, BorderLayout.CENTER);
        this.add(panel2, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();

        this.setLocationRelativeTo(null);

    }

    public static void main(String[] args){
        new MyFrame();
    }

}
