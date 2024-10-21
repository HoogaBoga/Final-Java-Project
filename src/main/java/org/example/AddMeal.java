package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMeal extends JButton implements ActionListener {
    private String mealName;
    private String category;
    private int servingSize;
    private String type;
    private int nutritionalValue;
    private String spice;
    private double mealPrice;
    private String ingredients;

    public AddMeal(){
        this.setText("Start");
        this.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        this.setForeground(new Color(0x654321));
        this.setBackground(new Color(0xFFDAA5));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.addActionListener(this);
    }

    public void setMeal(){

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        AddMealTable mealTable = new AddMealTable();

        mealTable.addMeal("Adobo", "Lunch", 10, "Non-Vegetarian", 300, "Spicy", 150.00, "Chicken");

    }

}
