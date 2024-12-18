package org.example.Misc;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {


        private int radius;


        public RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+3, this.radius+3, this.radius+3, this.radius+3);
        }


        public boolean isBorderOpaque() {
            return false;
        }


        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
