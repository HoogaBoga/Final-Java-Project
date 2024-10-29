package org.example;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class RoundedTextField extends JTextField {
    private int round = 25;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }

    public RoundedTextField() {
        setUI(new TextUI()); setOpaque(false);
        setForeground(new Color(80, 80, 80));
        setSelectedTextColor(Color.WHITE); setSelectionColor(new Color(133, 209, 255));
        setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double width = getWidth();
        double height = getHeight();
        // Create Background Color
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, round, round));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        repaint();
    }

    private class TextUI extends BasicTextFieldUI {
        @Override
        protected void paintBackground(Graphics grphcs) {
            // Do nothing to prevent default background painting
        }
    }
}
