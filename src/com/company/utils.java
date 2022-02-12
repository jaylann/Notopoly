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


}
