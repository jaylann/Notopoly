package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerInfo {

    private final Player player;
    private final UI parentUI;
    private final Board bp;
    private final String playerName;
    public PlayerInfo(Player player, Board bp, UI parentUI) {
        this.player = player;
        this.bp = bp;
        this.parentUI = parentUI;
        this.playerName = player.getName();
        scaleFactor = parentUI.getScaleFactor();

        outerRectX = (int) (150*scaleFactor);
        outerRectY = (int) (150*scaleFactor);
        outerRectWidth = (int) (400*scaleFactor);
        outerRectHeight = (int) (600*scaleFactor);
        outerRectArc = (int) (50*scaleFactor);

        infoTextX = (int) (outerRectX +(20*scaleFactor));
        infoTextY = (int) (outerRectY+(100*scaleFactor));

        menuStroke = new BasicStroke((float) (10*scaleFactor));
        textFont = new Font("Roboto", Font.PLAIN, (int) (25 * scaleFactor));
        headFont = new Font("Roboto", Font.PLAIN, (int) (45 * scaleFactor));
    }
    private final double scaleFactor;

    private final int outerRectX;
    private final int outerRectY;
    private final int outerRectWidth;
    private final int outerRectHeight;
    private final int outerRectArc;
    private final Stroke menuStroke;
    private final Font textFont;
    private final Font headFont;

    private final int infoTextX;
    private final int infoTextY;

    public void draw(Graphics2D g2) {
        g2.setStroke(menuStroke);
        g2.setColor(Color.black);
        g2.drawRoundRect(outerRectX,outerRectY, outerRectWidth,outerRectHeight,outerRectArc,outerRectArc);
        g2.setColor(Color.white);
        g2.fillRoundRect(outerRectX,outerRectY, outerRectWidth,outerRectHeight,outerRectArc,outerRectArc);
        drawPlayerName(g2);
    }
    private void drawPlayerName(Graphics2D g2) {
        g2.setFont(utils.getFittingFont(g2, playerName, headFont, outerRectWidth));
        g2.setColor(Color.black);
        g2.drawString(playerName, outerRectX+((outerRectWidth/2)-(utils.stringWidth(g2, playerName)/2)), outerRectY+50);
    }
    private void drawPlayerInfo(Graphics2D g2) {
        ArrayList<String> informationArray = new ArrayList<>(Arrays.asList("Geld", "Immobilien", ""));
    }
}
