package org.example.Frames;

import org.example.Buttons.CloseAddButton;
import org.example.Buttons.DashBoardButton;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AddMealFrame extends JFrame {

    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JLabel addMeal = new JLabel("Add Meal");
    private JLabel dishName = new JLabel("Dish Name");
    private JLabel mealType = new JLabel("Meal Type");
    private JLabel serveSize = new JLabel("Serving Size");
    private JLabel spicyLevel = new JLabel("Spiciness");
    private JLabel ingredientsNeed = new JLabel("Ingredients");
    private JLabel priceFood = new JLabel("Price");
    private JLabel amountFood = new JLabel("Amount");
    private JLabel addImg = new JLabel("Add Image +");

    public static Font loadCustomFont(){
        try {
            File fontFile = new File("Resources/Inter-VariableFont_opsz,wght.ttf");
            Font inter = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(inter);
            return inter;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
        }
    }

    public AddMealFrame() {

        Font inter = loadCustomFont();
        CloseAddButton closeAddButton = new CloseAddButton(this);

        RoundedTextField mealName = new RoundedTextField();
        mealName.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        mealName.setBackground(new Color(251, 250, 242));
        mealName.setForeground(Color.BLACK);
        mealName.setFont(inter.deriveFont(9f));
        mealName.setOpaque(false);
        mealName.setMargin(new Insets(0, 10, 0, 0));
        mealName.setPlaceholder("Enter dish name");

        RoundedTextField mealTypeText = new RoundedTextField();
        mealTypeText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        mealTypeText.setBackground(new Color(251, 250, 242));
        mealTypeText.setForeground(Color.BLACK);
        mealTypeText.setFont(inter.deriveFont(9f));
        mealTypeText.setOpaque(false);
        mealTypeText.setMargin(new Insets(0, 10, 0, 0));
        mealTypeText.setPlaceholder("Enter meal type");

        RoundedTextField serveSizeText = new RoundedTextField();
        serveSizeText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        serveSizeText.setBackground(new Color(251, 250, 242));
        serveSizeText.setForeground(Color.BLACK);
        serveSizeText.setFont(inter.deriveFont(9f));
        serveSizeText.setOpaque(false);
        serveSizeText.setMargin(new Insets(0, 10, 0, 0));
        serveSizeText.setPlaceholder("Enter serving size");

        RoundedTextField spiceLevelText = new RoundedTextField();
        spiceLevelText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        spiceLevelText.setBackground(new Color(251, 250, 242));
        spiceLevelText.setForeground(Color.BLACK);
        spiceLevelText.setFont(inter.deriveFont(9f));
        spiceLevelText.setOpaque(false);
        spiceLevelText.setMargin(new Insets(0, 10, 0, 0));
        spiceLevelText.setPlaceholder("Enter spiciness");

        RoundedTextField ingredientsNeedText = new RoundedTextField();
        ingredientsNeedText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        ingredientsNeedText.setBackground(new Color(251, 250, 242));
        ingredientsNeedText.setForeground(Color.BLACK);
        ingredientsNeedText.setFont(inter.deriveFont(9f));
        ingredientsNeedText.setOpaque(false);
        ingredientsNeedText.setMargin(new Insets(0, 10, 0, 0));
        ingredientsNeedText.setPlaceholder("Enter ingredients");

        RoundedTextField priceFoodText = new RoundedTextField();
        priceFoodText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        priceFoodText.setBackground(new Color(251, 250, 242));
        priceFoodText.setForeground(Color.BLACK);
        priceFoodText.setFont(inter.deriveFont(9f));
        priceFoodText.setOpaque(false);
        priceFoodText.setMargin(new Insets(0, 10, 0, 0));
        priceFoodText.setPlaceholder("Enter price");

        RoundedTextField amountFoodText = new RoundedTextField();
        amountFoodText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        amountFoodText.setBackground(new Color(251, 250, 242));
        amountFoodText.setForeground(Color.BLACK);
        amountFoodText.setFont(inter.deriveFont(9f));
        amountFoodText.setOpaque(false);
        amountFoodText.setMargin(new Insets(0, 10, 0, 0));
        amountFoodText.setPlaceholder("Enter price");


        // Set BorderLayout for AddMealFrame
        this.setLayout(new BorderLayout());

        addMeal.setFont(inter.deriveFont(Font.BOLD,12f));
        addMeal.setForeground(Color.WHITE);
        addMeal.setHorizontalAlignment(SwingConstants.CENTER);

        closeAddButton.setPreferredSize(new Dimension(19, 19));
        closeAddButton.setMaximumSize(new Dimension(19, 19));

        dishName.setFont(inter.deriveFont(12f));
        dishName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        mealType.setFont(inter.deriveFont(12f));
        mealType.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));

        serveSize.setFont(inter.deriveFont(12f));
        serveSize.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));

        spicyLevel.setFont(inter.deriveFont(12f));
        spicyLevel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 19));

        ingredientsNeed.setFont(inter.deriveFont(12f));
        ingredientsNeed.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        priceFood.setFont(inter.deriveFont(12f));
        priceFood.setBorder(BorderFactory.createEmptyBorder(0,0,0,45));

        amountFood.setFont(inter.deriveFont(12f));
        amountFood.setBorder(BorderFactory.createEmptyBorder(0,0,0,30 ));

        topPanel.setBackground(new Color(0x58A558));
        topPanel.setPreferredSize(new Dimension(241, 25));
        topPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.weightx = 1.0; // Allow to grow
        gbc.insets = new Insets(0, 10, 0, 0);
        topPanel.add(addMeal, gbc);

        gbc.gridx = 1; // Second column for the button
        gbc.weightx = 0; // Fixed size
        gbc.insets = new Insets(0, 0, 0, 5);
        topPanel.add(closeAddButton, gbc);



        centerPanel.setLayout(new FlowLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(dishName);
        centerPanel.add(mealName);
        centerPanel.add(mealType);
        centerPanel.add(mealTypeText);
        centerPanel.add(serveSize);
        centerPanel.add(serveSizeText);
        centerPanel.add(spicyLevel);
        centerPanel.add(spiceLevelText);
        centerPanel.add(ingredientsNeed);
        centerPanel.add(ingredientsNeedText);
        centerPanel.add(priceFood);
        centerPanel.add(priceFoodText);
        centerPanel.add(amountFood);
        centerPanel.add(amountFoodText);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setSize(241, 326); // Set a fixed size for the frame
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
