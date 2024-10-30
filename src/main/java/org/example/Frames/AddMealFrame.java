package org.example.Frames;

import org.example.Buttons.DashBoardButton;
import org.example.TextFields.RoundedTextField;

import javax.swing.*;
import java.awt.*;

public class AddMealFrame extends JFrame {

    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();
    private JLabel addMeal = new JLabel("Add Meal");

    public AddMealFrame(){

        RoundedTextField mealName = new RoundedTextField();
        mealName.setBounds(79,0, 62, 15);
        mealName.setBackground(new Color(251, 250, 242));
        mealName.setForeground(Color.BLACK);
        mealName.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(10f));
        mealName.setOpaque(false);
        mealName.setMargin(new Insets(0, 10, 0, 0));
        mealName.setPlaceholder("Search");

        this.setLayout(null);

        addMeal.setFont(new Font("Arial", Font.BOLD, 12));

        topPanel.setBackground(new Color(0x58A558));
        topPanel.setBounds(0, 0, 241, 25);
        topPanel.add(addMeal, BorderLayout.CENTER);

        centerPanel.setLayout(null);
        centerPanel.add(mealName);


        this.add(topPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
