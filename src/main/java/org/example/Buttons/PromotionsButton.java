package org.example.Buttons;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PromotionsButton extends JButton {

    public PromotionsButton(CardLayout cardLayout, JPanel cardPanel, String panelName){
        super("   Promotions");

        ImageIcon promotionsIcon = new ImageIcon(Objects.requireNonNull(PromotionsButton.class.getResource("/PromotionGreen.png")));

        this.setBounds(12, 147, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);
        this.setIcon(promotionsIcon);
        this.setFont(DashBoardButton.ACTOR_REGULAR_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));

        this.setIconTextGap(-2);

        this.addActionListener(e -> cardLayout.show(cardPanel, "Promotions"));
    }
}
