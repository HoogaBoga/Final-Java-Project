package org.example;

import javax.swing.*;
import java.awt.*;

public class SalesButton extends JButton {

    public SalesButton(){
        super("Sales    ");

        ImageIcon salesIcon = new ImageIcon("Resources/SalesGreen.png");

        this.setBounds(12, 101, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(salesIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(14);
    }
}
