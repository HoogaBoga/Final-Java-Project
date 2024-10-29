package org.example;

import javax.swing.*;
import java.awt.*;

public class OrdersButton extends JButton {

    public OrdersButton(){
        super("Orders    ");

        ImageIcon ordersIcon = new ImageIcon("Resources/OrdersGreen.png");

        this.setBounds(12, 78, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(ordersIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(7);
    }
}
