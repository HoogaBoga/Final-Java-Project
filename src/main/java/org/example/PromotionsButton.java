package org.example;

import javax.swing.*;
import java.awt.*;

public class PromotionsButton extends JButton {

    public PromotionsButton(){
        super("   Promotions");

        ImageIcon promotionsIcon = new ImageIcon("Resources/PromotionGreen.png");

        this.setBounds(12, 124, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(promotionsIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(-2);
    }
}
