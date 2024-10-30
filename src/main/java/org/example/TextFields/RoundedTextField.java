package org.example.TextFields;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class RoundedTextField extends JTextField {
    private int round = 25;
    private String placeholder;
    private boolean isInitiallyFocusable = false;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setText(placeholder);
        setForeground(Color.GRAY);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(placeholder);
                }
                setFocusable(false); // Make non-focusable again
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isInitiallyFocusable) {
                    setFocusable(true);
                    requestFocusInWindow();
                }
            }
        });

        setFocusable(isInitiallyFocusable);
    }

    public RoundedTextField() {
        setUI(new TextUI());
        setOpaque(false);
        setForeground(new Color(80, 80, 80));
        setSelectedTextColor(Color.WHITE);
        setSelectionColor(new Color(133, 209, 255));
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
