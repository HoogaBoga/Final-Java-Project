package org.example.Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class InventoryButton extends JButton {

    public InventoryButton(CardLayout cardLayout, JPanel cardPanel, String panelName){
        super(" Inventory  ");

        ImageIcon ordersIcon = new ImageIcon(Objects.requireNonNull(InventoryButton.class.getResource("/InventoryGreen.png")));

        this.setBounds(12, 101, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(ordersIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(3);

        this.addActionListener(e -> cardLayout.show(cardPanel, panelName));
    }
}
