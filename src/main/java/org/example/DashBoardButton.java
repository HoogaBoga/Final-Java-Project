package org.example;

import javax.swing.*;
import java.awt.*;

public class DashBoardButton extends JButton {

    public DashBoardButton(){
        super("Dashboard");
        this.setBounds(12, 55, 86, 16);
        this.setBackground(new Color(152, 130, 132));
        this.setForeground(Color.BLACK);

        this.setFont(FigmaToCodeApp.READEX_PRO_FONT.deriveFont(8f));
        this.setFocusable(false);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(new Color(0xBCBCBC)));
    }
}
