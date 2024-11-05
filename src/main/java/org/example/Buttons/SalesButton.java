package org.example.Buttons;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SalesButton extends JButton {

    public SalesButton(CardLayout cardLayout, JPanel cardPanel, String panelName){
        super("Sales    ");

        ImageIcon salesIcon = new ImageIcon(Objects.requireNonNull(SalesButton.class.getResource("/SalesGreen.png")));

        this.setBounds(12, 124, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(salesIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(14);

        this.addActionListener(e -> cardLayout.show(cardPanel, "Sales"));
    }
}
