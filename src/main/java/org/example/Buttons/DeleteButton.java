package org.example.Buttons;

import org.example.Frames.AddMealFrame;
import org.example.Misc.CrudMeal;
import org.example.Misc.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteButton extends JButton{

    public DeleteButton(){
        this.setText("Delete");
        this.setForeground(Color.WHITE);
        this.setFont(AddMealFrame.loadCustomFont().deriveFont(Font.BOLD, 12f));
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.CENTER);

        this.setPreferredSize(new Dimension(91, 25));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setBorder(new RoundedBorder(14));
        this.setBackground(new Color(0xFF7979));
        this.setFocusable(false);

    }

}


