package org.example.Misc;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class DropShadowBorder extends AbstractBorder {
    private final int shadowSize;
    private final Color shadowColor;
    private final Color lineColor;

    public DropShadowBorder(int shadowSize, Color shadowColor, Color lineColor) {
        this.shadowSize = shadowSize;
        this.shadowColor = shadowColor;
        this.lineColor = lineColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadowOffset = 2;

        // Draw shadow on the right, left, and bottom sides
        for (int i = shadowSize; i > 0; i--) {
            g2d.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), (int) (50 * (1 - i / (float) shadowSize))));
            // Right shadow
            g2d.fillRect(x + width - i, y + shadowOffset, i, height - shadowOffset);
            // Left shadow
            g2d.fillRect(x - shadowSize + i, y + shadowOffset, i, height - shadowOffset);
            // Bottom shadow
            g2d.fillRect(x + shadowOffset, y + height - i, width - shadowOffset, i);
        }

        // Draw the panel border line
        g2d.setColor(lineColor);
        g2d.drawRect(x, y, width - shadowSize, height - shadowSize);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, shadowSize, shadowSize, shadowSize); // Space for shadow on left, right, and bottom
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 0;
        insets.left = shadowSize;
        insets.bottom = shadowSize;
        insets.right = shadowSize;
        return insets;
    }
}
