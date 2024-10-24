package org.example;

import javax.swing.*;
import java.awt.*;

public class ShadowLabel extends JLabel {

    private Color shadowColor = new Color(0, 0, 0, 50);
    private int shadowOffsetX = 2;
    private int shadowOffsetY = 2;
    private int shadowBlur = 5;

    public ShadowLabel(String text) {
        super(text);
    }

    public void setShadow(Color color, int offsetX, int offsetY, int blur) {
        this.shadowColor = color;
        this.shadowOffsetX = offsetX;
        this.shadowOffsetY = offsetY;
        this.shadowBlur = blur;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother text and shadow
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < shadowBlur; i++) {
            g2d.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), shadowColor.getAlpha() / (i + 1)));
            g2d.drawString(getText(), getInsets().left + shadowOffsetX + i, getInsets().top + g2d.getFontMetrics().getAscent() + shadowOffsetY + i);
        }

        // Draw the actual label text on top
        g2d.setColor(getForeground());
        g2d.drawString(getText(), getInsets().left, getInsets().top + g2d.getFontMetrics().getAscent());

        g2d.dispose();
    }
}
