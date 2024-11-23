package org.example.Frames;

import org.example.Buttons.CloseSortButton;
import org.example.Buttons.DashBoardButton;
import org.example.Misc.DropShadowBorder;
import org.example.Panels.DashBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SortingFrame extends JFrame {

    private DashBoardPanel dashBoardPanel;

    public SortingFrame(DashBoardPanel dashBoardPanel){
        this.dashBoardPanel = dashBoardPanel;

        CloseSortButton closeSortButton = new CloseSortButton(this);
        closeSortButton.setPreferredSize(new Dimension(19, 19));

        // Top panel with BorderLayout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(350, 29));
        topPanel.setBackground(new Color(0x58A558));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding to the top panel

        // Icons
        ImageIcon greenImg = new ImageIcon(Objects.requireNonNull(SortingFrame.class.getResource("/Sortpic.png")));
        ImageIcon sortText = new ImageIcon(Objects.requireNonNull(SortingFrame.class.getResource("/Sort.png")));
        ImageIcon unchecked = new ImageIcon(Objects.requireNonNull(SortingFrame.class.getResource("/Unchecked.png")));
        ImageIcon checked = new ImageIcon(Objects.requireNonNull(SortingFrame.class.getResource("/Checked.png")));

        // Labels for icons
        JLabel greenImgLabel = new JLabel(greenImg);
        JLabel sortTextLabel = new JLabel(sortText);

        // Sub-panel for greenImg and sortText
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Add spacing
        leftPanel.setOpaque(false);
        leftPanel.add(greenImgLabel);
        leftPanel.add(sortTextLabel);

        // Add components to the top panel
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(closeSortButton, BorderLayout.EAST);

        // Main center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        centerPanel.setBackground(Color.WHITE);

        JLabel mealCategory = new JLabel("Meal Category");
        mealCategory.setBounds(12, 20, 100, 15);
        mealCategory.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        centerPanel.add(mealCategory);

        // Panel for "Breakfast" with drop shadow
        JPanel breakFast = createCheckboxPanel(unchecked, checked, "Breakfast", 12, 45, 102, 37);
        JCheckBox breakFastCheck = (JCheckBox) breakFast.getComponent(0);
        centerPanel.add(breakFast);

        // Panel for "Lunch" with drop shadow
        JPanel lunch = createCheckboxPanel(unchecked, checked, "Lunch", 125, 45, 83, 37);
        JCheckBox lunchCheck = (JCheckBox) lunch.getComponent(0);
        centerPanel.add(lunch);

        // Panel for "Dinner" with drop shadow
        JPanel dinner = createCheckboxPanel(unchecked, checked, "Dinner", 220, 45, 85, 37);
        JCheckBox dinnerCheck = (JCheckBox) dinner.getComponent(0);
        centerPanel.add(dinner);

        // Spiciness Levels Label
        JLabel spicinessLabel = new JLabel("Spiciness Levels");
        spicinessLabel.setBounds(12, 113, 150, 15); // Adjust position to avoid overlap
        spicinessLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        spicinessLabel.setForeground(Color.BLACK); // Ensure text is visible
        centerPanel.add(spicinessLabel);

        JPanel spicy = createCheckboxPanel(unchecked, checked, "Spicy", 12, 138, 80, 37);
        JCheckBox spicyCheck = (JCheckBox) spicy.getComponent(0);
        centerPanel.add(spicy);

        JPanel nonSpicy = createCheckboxPanel(unchecked, checked, "Non-Spicy", 103, 138, 108, 37);
        JCheckBox nonSpicyCheck = (JCheckBox) nonSpicy.getComponent(0);
        centerPanel.add(nonSpicy);

        JLabel dietLabel = new JLabel("Meal Diet");
        dietLabel.setBounds(12, 206, 150, 15); // Adjust position to avoid overlap
        dietLabel.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        dietLabel.setForeground(Color.BLACK); // Ensure text is visible
        centerPanel.add(dietLabel);

        JPanel vegetarian = createCheckboxPanel(unchecked, checked, "Vegetarian", 12, 231,109, 37);
        JCheckBox vegetarianCheck = (JCheckBox) vegetarian.getComponent(0);
        centerPanel.add(vegetarian);

        JPanel nonVegetarian = createCheckboxPanel(unchecked, checked, "Non-Vegetarian", 133, 231, 138, 37);
        JCheckBox nonVegetarianCheck = (JCheckBox) nonVegetarian.getComponent(0);
        centerPanel.add(nonVegetarian);

        JButton sortButton = new JButton("Sort");
        sortButton.setBounds(250, 285, 76, 21);
        sortButton.setBackground(new Color(0x987284));
        sortButton.setForeground(Color.WHITE);
        sortButton.setFocusPainted(false);
        sortButton.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        centerPanel.add(sortButton);

        sortButton.addActionListener(e -> {
            java.util.List<String> selectedCategories = new ArrayList<>();
            if (breakFastCheck.isSelected()) {
                selectedCategories.add("Breakfast");
            }
            if (lunchCheck.isSelected()) {
                selectedCategories.add("Lunch");
            }
            if (dinnerCheck.isSelected()) {
                selectedCategories.add("Dinner");
            }

            String selectedSpiciness = null;
            if (spicyCheck.isSelected()) {
                selectedSpiciness = "Spicy";
            } else if (nonSpicyCheck.isSelected()) {
                selectedSpiciness = "Non-Spicy";
            }

            String selectedDiet = null;
            if (vegetarianCheck.isSelected()) {
                selectedDiet = "Vegetarian";
            } else if (nonVegetarianCheck.isSelected()) {
                selectedDiet = "Non-Vegetarian";
            }

            // Pass filters to DashBoardPanel
            dashBoardPanel.loadDataInBackground(selectedCategories, selectedSpiciness, selectedDiet);

            // Close the SortingFrame

            this.dispose();
        });


        // Add logic to unselect all if all are selected
        categoryCheckAll(breakFastCheck, lunchCheck, dinnerCheck);
        categoryCheckEither(spicyCheck, nonSpicyCheck);
        categoryCheckEither(vegetarianCheck, nonVegetarianCheck);

        // Add topPanel and centerPanel to the frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Frame settings
        setSize(337, 345); // Increase height to ensure visibility
        setResizable(false);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a JPanel containing a JCheckBox with custom icons and a drop shadow.
     */
    private JPanel createCheckboxPanel(ImageIcon unchecked, ImageIcon checked, String text, int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height); // Use dynamic width and height
        panel.setBackground(Color.WHITE);
        panel.setBorder(new DropShadowBorder(5, Color.BLACK, Color.WHITE));

        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setBackground(Color.WHITE);
        checkBox.setFocusPainted(false);
        checkBox.setIcon(unchecked);
        checkBox.setSelectedIcon(checked);
        checkBox.setFont(AddMealFrame.INTER_FONT.deriveFont(Font.PLAIN, 12f));
        panel.setLayout(new GridBagLayout());
        panel.add(checkBox);

        return panel;
    }


    /**
     * Adds functionality to uncheck all checkboxes if all are selected.
     */
    public void categoryCheckAll(JCheckBox... checkBoxes) {
        // Logic for individual checkboxes
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.addActionListener(e -> {
                boolean allSelected = true;
                for (JCheckBox box : checkBoxes) {
                    if (!box.isSelected()) {
                        allSelected = false;
                        break;
                    }
                }
                // If all are selected, uncheck all
                if (allSelected) {
                    for (JCheckBox box : checkBoxes) {
                        box.setSelected(false);
                    }
                }
            });
        }
    }

    public void categoryCheckEither(JCheckBox... checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.addActionListener(e -> {
                // Deselect all other checkboxes when one is selected
                for (JCheckBox box : checkBoxes) {
                    if (box != checkBox) {
                        box.setSelected(false); // Deselect other checkboxes
                    }
                }
            });
        }
    }
}
