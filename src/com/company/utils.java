package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class utils {
    public static Image loadImage(String path, int width, int height) {
        try {
            return ImageIO.read(new File(path)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) { throw new RuntimeException("This file should always exist. Unless someone intentionally deleted it.",e); }
    }

    public static int stringWidth(Graphics2D g2, String s) {
        return g2.getFontMetrics().stringWidth(s);
    }

    public static Font getFittingFont(Graphics2D g2, String s, Font f, int width) {
        String fontName = f.getFontName();
        int fontSize = f.getSize();
        int fontStyle = f.getStyle();
        boolean found = false;
        Font testFont = f;
        while (!found) {

            g2.setFont(testFont);
            if (stringWidth(g2,s) <= width) {
                found = true;
            } else {
                fontSize--;
                testFont = new Font(fontName,fontStyle,fontSize);
            }

        }
        return testFont;
    }


}
