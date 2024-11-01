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

public class RoundedPassWordField extends JPasswordField {
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

        // Focus listener to manage placeholder text and color
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(getPassword()).equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar('\u2022'); // Set echo character when focused
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(getPassword()).isEmpty()) {
                    setForeground(Color.GRAY);
                    setText(placeholder);
                    setEchoChar((char) 0); // Remove echo character to show placeholder
                }
                setFocusable(false); // Make non-focusable again
            }
        });

        // Mouse listener to allow click-to-focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isInitiallyFocusable) {
                    setFocusable(true);
                    requestFocusInWindow();
                }
            }
        });

        setFocusable(isInitiallyFocusable); // Initially non-focusable
    }

    public RoundedPassWordField() {
        setUI(new TextUI());
        setOpaque(false);
        setForeground(new Color(80, 80, 80));
        setSelectedTextColor(Color.WHITE);
        setSelectionColor(new Color(133, 209, 255));
        setBorder(new EmptyBorder(5, 10, 5, 10));

        // Set the initial echo character to hide the password input
        setEchoChar('\u2022');
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double width = getWidth();
        double height = getHeight();

        // Draw the rounded rectangle as background
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

    // Custom UI class to prevent default background painting
    private class TextUI extends BasicTextFieldUI {
        @Override
        protected void paintBackground(Graphics grphcs) {
            // Do nothing to prevent default background painting
        }
    }
}
