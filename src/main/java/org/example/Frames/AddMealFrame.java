package org.example.Frames;

import com.sun.jdi.IntegerValue;
import org.example.Buttons.AddImageLabel;
import org.example.Buttons.CloseAddButton;
import org.example.Buttons.DashBoardButton;
import org.example.Buttons.FinalAddButton;
import org.example.Misc.CrudInventory;
import org.example.Misc.CrudMeal;
import org.example.Misc.DatabaseManager;
import org.example.Panels.DashBoardPanel;
import org.example.TextFields.RoundedTextField;

import javax.imageio.ImageIO;
import javax.management.StringValueExp;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class AddMealFrame extends JFrame {

    private DashBoardPanel dashBoardPanel = new DashBoardPanel();

    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JLabel addMeal = new JLabel("Add Meal");
    private JLabel dishName = new JLabel("Dish Name");
    private JLabel mealCategory = new JLabel("Category");
    private JLabel mealType = new JLabel("Meal Type");
    private JLabel nutritional = new JLabel("Nutrition");
    private JLabel serveSize = new JLabel("Serving Size");
    private JLabel spicyLevel = new JLabel("Spiciness");
    private JLabel ingredientsNeed = new JLabel("Ingredients");
    private JLabel priceFood = new JLabel("Price");
    private JLabel amountFood = new JLabel("Amount");
    private JLabel mealID = new JLabel("Meal ID");
    private AddImageLabel addImageLabel = new AddImageLabel();
    private JLabel spacer = new JLabel();
    private JLabel spacer2 = new JLabel();
    private FinalAddButton finalAddButton = new FinalAddButton();
    private RoundedTextField mealName = new RoundedTextField();
    private RoundedTextField mealCategories = new RoundedTextField();
    private RoundedTextField serveSizeText = new RoundedTextField();
    private RoundedTextField mealTypeText = new RoundedTextField();
    private RoundedTextField nutritionalValue = new RoundedTextField();
    private RoundedTextField spiceLevelText = new RoundedTextField();
    private RoundedTextField ingredientsNeedText = new RoundedTextField();
    private RoundedTextField priceFoodText = new RoundedTextField();
    private RoundedTextField amountFoodText = new RoundedTextField();
    private RoundedTextField mealIDText = new RoundedTextField();
    private int userID;
    private CrudMeal addMeals = new CrudMeal(dashBoardPanel);
    private CrudInventory addInventory = new CrudInventory();


    public static final Font INTER_FONT = loadCustomFont();


    public static Font loadCustomFont() {
        try (InputStream is = AddMealFrame.class.getResourceAsStream("/Inter-VariableFont_opsz,wght.ttf")) {
            if (is == null) {
                System.err.println("Font file not found in resources.");
                return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
            }
            Font inter = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(inter);
            return inter;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14); // Fallback font
        }
    }

    public AddMealFrame(DashBoardPanel dashBoardPanel, int userID) {

        this.userID = userID;
        Font inter = loadCustomFont();
        CloseAddButton closeAddButton = new CloseAddButton(this);

        mealName.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        mealName.setBackground(new Color(251, 250, 242));
        mealName.setForeground(Color.BLACK);
        mealName.setFont(inter.deriveFont(9f));
        mealName.setOpaque(false);
        mealName.setMargin(new Insets(0, 10, 0, 0));
        mealName.setPlaceholder("Enter dish name");

        mealCategories.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        mealCategories.setBackground(new Color(251, 250, 242));
        mealCategories.setForeground(Color.BLACK);
        mealCategories.setFont(inter.deriveFont(9f));
        mealCategories.setOpaque(false);
        mealCategories.setMargin(new Insets(0, 10, 0, 0));
        mealCategories.setPlaceholder("Enter meal category");

        mealTypeText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        mealTypeText.setBackground(new Color(251, 250, 242));
        mealTypeText.setForeground(Color.BLACK);
        mealTypeText.setFont(inter.deriveFont(9f));
        mealTypeText.setOpaque(false);
        mealTypeText.setMargin(new Insets(0, 10, 0, 0));
        mealTypeText.setPlaceholder("Enter meal type");

        nutritionalValue.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        nutritionalValue.setBackground(new Color(251, 250, 242));
        nutritionalValue.setForeground(Color.BLACK);
        nutritionalValue.setFont(inter.deriveFont(9f));
        nutritionalValue.setOpaque(false);
        nutritionalValue.setMargin(new Insets(0, 10, 0, 0));
        nutritionalValue.setPlaceholder("Enter nutritional value");

        serveSizeText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        serveSizeText.setBackground(new Color(251, 250, 242));
        serveSizeText.setForeground(Color.BLACK);
        serveSizeText.setFont(inter.deriveFont(9f));
        serveSizeText.setOpaque(false);
        serveSizeText.setMargin(new Insets(0, 10, 0, 0));
        serveSizeText.setPlaceholder("Enter serving size");

        spiceLevelText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        spiceLevelText.setBackground(new Color(251, 250, 242));
        spiceLevelText.setForeground(Color.BLACK);
        spiceLevelText.setFont(inter.deriveFont(9f));
        spiceLevelText.setOpaque(false);
        spiceLevelText.setMargin(new Insets(0, 10, 0, 0));
        spiceLevelText.setPlaceholder("Enter spiciness");

        ingredientsNeedText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        ingredientsNeedText.setBackground(new Color(251, 250, 242));
        ingredientsNeedText.setForeground(Color.BLACK);
        ingredientsNeedText.setFont(inter.deriveFont(9f));
        ingredientsNeedText.setOpaque(false);
        ingredientsNeedText.setMargin(new Insets(0, 10, 0, 0));
        ingredientsNeedText.setPlaceholder("Enter ingredients");

        priceFoodText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        priceFoodText.setBackground(new Color(251, 250, 242));
        priceFoodText.setForeground(Color.BLACK);
        priceFoodText.setFont(inter.deriveFont(9f));
        priceFoodText.setOpaque(false);
        priceFoodText.setMargin(new Insets(0, 10, 0, 0));
        priceFoodText.setPlaceholder("Enter price");

        amountFoodText.setPreferredSize(new Dimension(147, 20)); // Set preferred size for visibility
        amountFoodText.setBackground(new Color(251, 250, 242));
        amountFoodText.setForeground(Color.BLACK);
        amountFoodText.setFont(inter.deriveFont(9f));
        amountFoodText.setOpaque(false);
        amountFoodText.setMargin(new Insets(0, 10, 0, 0));
        amountFoodText.setPlaceholder("Enter amount");

        this.setLayout(new BorderLayout());

        finalAddButton.addActionListener(e -> {
            try {
                String mealNameInput = mealName.getText();
                String mealCategoryInput = mealCategories.getText();
                int servingSizeInput = Integer.parseInt(serveSizeText.getText());
                String mealTypeInput = mealTypeText.getText();
                int mealNutritionalInput = Integer.parseInt(nutritionalValue.getText());
                String spiceLevelInput = spiceLevelText.getText();
                String ingredientsInput = ingredientsNeedText.getText();
                File imageFile = addImageLabel.getSelectedImageFile();

                double mealPriceInput = Double.parseDouble(priceFoodText.getText());
                int quantityInput = Integer.parseInt(amountFoodText.getText());

                // Add meal and retrieve generated mealID
                int mealID = addMeals.addMeal(mealNameInput, mealCategoryInput, servingSizeInput, mealTypeInput,
                        mealNutritionalInput, spiceLevelInput, ingredientsInput, imageFile);

                if (mealID != -1) {
                    // Use the retrieved mealID to add to inventory
                    addInventory.addInventory(quantityInput, mealPriceInput, mealID);
                    String price = String.valueOf(mealPriceInput);
                    Image image = ImageIO.read(imageFile);
                    ImageIcon image2 = new ImageIcon(image);
                    dashBoardPanel.contentPanel.add(dashBoardPanel.createItemPanel(mealID, mealNameInput, price, image2, mealTypeInput, mealCategoryInput));

                    dashBoardPanel.setNeedsRefresh(true);

                    this.dispose();

                } else {
                    System.out.println("Failed to add meal. Inventory entry was not created.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for serving size, nutritional value, price, and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        addMeal.setFont(inter.deriveFont(Font.BOLD,12f));
        addMeal.setForeground(Color.WHITE);
        addMeal.setHorizontalAlignment(SwingConstants.CENTER);

        closeAddButton.setPreferredSize(new Dimension(19, 19));
        closeAddButton.setMaximumSize(new Dimension(19, 19));

        dishName.setFont(inter.deriveFont(12f));
        dishName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        mealCategory.setFont(inter.deriveFont(12f));
        mealCategory.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));

        mealType.setFont(inter.deriveFont(12f));
        mealType.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));

        serveSize.setFont(inter.deriveFont(12f));
        serveSize.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));

        nutritional.setFont(inter.deriveFont(12f));
        nutritional.setBorder(BorderFactory.createEmptyBorder(0,0,0,25));

        spicyLevel.setFont(inter.deriveFont(12f));
        spicyLevel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 19));

        ingredientsNeed.setFont(inter.deriveFont(12f));
        ingredientsNeed.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        priceFood.setFont(inter.deriveFont(12f));
        priceFood.setBorder(BorderFactory.createEmptyBorder(0,0,0,45));

        amountFood.setFont(inter.deriveFont(12f));
        amountFood.setBorder(BorderFactory.createEmptyBorder(0,0,0,30 ));

        spacer.setPreferredSize(new Dimension(241, 5));
        spacer2.setPreferredSize(new Dimension(241, 5));

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
        centerPanel.add(mealCategory);
        centerPanel.add(mealCategories);
        centerPanel.add(serveSize);
        centerPanel.add(serveSizeText);
        centerPanel.add(mealType);
        centerPanel.add(mealTypeText);
        centerPanel.add(nutritional);
        centerPanel.add(nutritionalValue);
        centerPanel.add(spicyLevel);
        centerPanel.add(spiceLevelText);
        centerPanel.add(ingredientsNeed);
        centerPanel.add(ingredientsNeedText);
        if(isEmployee(userID)){
            priceFoodText.setPlaceholder("Enter Price");
            priceFoodText.setText("0");
            priceFoodText.setEditable(false);

            amountFoodText.setPlaceholder("Enter Price");
            amountFoodText.setText("0");
            amountFoodText.setEditable(false);
        }
        centerPanel.add(priceFood);
        centerPanel.add(priceFoodText);
        centerPanel.add(amountFood);
        centerPanel.add(amountFoodText);
        centerPanel.add(spacer);
        centerPanel.add(addImageLabel);
        centerPanel.add(spacer2);
        centerPanel.add(finalAddButton);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setSize(241, 360); // Set a fixed size for the frame
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private boolean isEmployee(int userId){
        String query = "SELECT role FROM Users WHERE id = ?";

        try(Connection connection = DatabaseManager.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String role = resultSet.getString("role").trim();
                System.out.println("DEBUG: Role retrieved from database for userId " + userId + " is: " + role);
                return "Employee".equalsIgnoreCase(role);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

}
