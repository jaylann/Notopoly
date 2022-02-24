package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Info {
    protected Property infoProperty;
    protected String propertyName;

    protected int price;
    
    //<editor-fold desc="Dependencies">
    protected final UI parentUI;
    protected final double scaleFactor;
    //</editor-fold>

    //<editor-fold desc="Fonts">
    protected final Font headFont;
    protected final Font textFont;
    protected final Font smallTextFont;
    protected final Font semiHeadFont;
    //</editor-fold>

    //<editor-fold desc="Colors">
    protected final Color innerStreetInfoColor = new Color(229, 234, 229, 250);
    protected final Color buyColor = new Color(12, 156, 13);
    protected final Color continueColor = new Color(217, 53, 53);
    //</editor-fold>

    //<editor-fold desc="Strokes">
    protected final Stroke streetInfoStroke;
    protected final Stroke streetInfoInnerStroke;
    protected final Stroke cutStroke;
    //</editor-fold>

    //<editor-fold desc="Outer Rectangle Values">
    protected final int outerRectX;
    protected final int outerRectY;
    protected final int outerRectWidth;
    protected final int outerRectHeight;
    protected final int outerRectArc;
    //</editor-fold>

    //<editor-fold desc="Inner Rectangle Values">
    protected final int innerRectX;
    protected final int innerRectY;
    protected final int innerRectWidth;
    protected final int innerRectHeight;
    protected final int innerRectArc;
    //</editor-fold>

    //<editor-fold desc="Street Name Values">
    protected final int streetNameX;
    protected final int streetNameY;
    protected final int streetNameWidth;
    protected final int streetNameHeight;
    //</editor-fold>

    //<editor-fold desc="Info Text Values">
    protected final int infoTextX;
    protected final int infoTextY;
    protected final int infoTextCostX;
    protected final int infoTextCostY;
    //</editor-fold>

    //<editor-fold desc="Button Values">
    protected final int buyButtonX;
    protected int passButtonX;
    protected final int buttonY;
    protected final int buttonWidth;
    protected final int buttonHeight;
    protected final int buttonArc;
    protected JButton buyButton;
    protected JButton passButton;

    //</editor-fold>

    protected Info(UI pUI) {
        parentUI = pUI;
        scaleFactor = parentUI.getScaleFactor();

        //<editor-fold desc="Fonts">
        headFont = new Font("Roboto", Font.PLAIN, (int) (35 * scaleFactor));
        textFont = new Font("Roboto", Font.PLAIN, (int) (20 * scaleFactor));
        smallTextFont = new Font("Roboto", Font.PLAIN, (int) (15 * scaleFactor));
        semiHeadFont = new Font("Roboto", Font.PLAIN, (int) (30 * scaleFactor));
        //</editor-fold>

        //<editor-fold desc="Strokes">
        streetInfoInnerStroke = new BasicStroke((float) (10 * scaleFactor));
        streetInfoStroke = new BasicStroke((float) (10 * scaleFactor));
        cutStroke = new BasicStroke(1);
        //</editor-fold>

        //<editor-fold desc="Outer Rectangle Values">
        outerRectX = (int) (600 * scaleFactor);
        outerRectY = (int) (350 * scaleFactor);
        outerRectHeight = (int) (455 * scaleFactor);
        outerRectWidth = (int) (325 * scaleFactor);
        outerRectArc = (int) (50 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Inner Rectangle Values">
        innerRectX = (int) (outerRectX + (25 * scaleFactor));
        innerRectY = (int) (outerRectY + (25 * scaleFactor));
        innerRectWidth = (int) (outerRectWidth - (50 * scaleFactor));
        innerRectHeight = (int) (75 * scaleFactor);
        innerRectArc = (int) (25 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Street Name Values">
        streetNameX = innerRectX;
        streetNameY = (int) (innerRectY + 12.5 * scaleFactor);
        streetNameWidth = innerRectWidth / 2;
        streetNameHeight = (int) (innerRectHeight / 2.0);
        //</editor-fold>

        //<editor-fold desc="Info Text Values">
        infoTextX = streetNameX;
        infoTextY = (int) ((375 + (75 / 2)-5) * scaleFactor);
        infoTextCostX = (int) (900 * scaleFactor);
        infoTextCostY = infoTextY;
        //</editor-fold>

        //<editor-fold desc="Button Values">
        buttonY = (int) (infoTextY + 315 * scaleFactor);
        passButtonX = (int) (775 * scaleFactor);
        buyButtonX = (int) (625 * scaleFactor);
        buttonWidth = (int) (125 * scaleFactor);
        buttonHeight = (int) (60 * scaleFactor);
        buttonArc = (int) (15 * scaleFactor);
        //</editor-fold>


        
    }



    abstract void draw(Graphics2D g2);

    public ArrayList<JButton> getButtons() {
        return new ArrayList<>(Arrays.asList(buyButton, passButton));
    }

    protected void drawButtons(@NotNull Graphics2D g2) {
        //Drawing Buttons
        g2.setFont(textFont);
        g2.setColor(buyColor);
        g2.fillRoundRect(buyButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

        g2.setColor(continueColor);
        g2.fillRoundRect(passButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

        //Drawing text on Buttons
        g2.setColor(Color.white);
        g2.drawString("Kaufen",
                (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Kaufen") / 2),
                (int) (buttonY + 25 * scaleFactor));

        g2.drawString(String.format("%d€", price),
                (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, String.format("%d€", price)) / 2),
                (int) (buttonY + 50 * scaleFactor));

        g2.drawString("Weiter",
                (passButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Weiter") / 2),
                (int) (buttonY + 37 * scaleFactor));
    }
    protected void drawStreetName(@NotNull Graphics2D g2) {
        g2.setFont(headFont);
        g2.setColor(Color.black);
        if (utils.stringWidth(g2, propertyName) > innerRectWidth) {
            g2.setFont(semiHeadFont);
        }
        g2.drawString(propertyName,
                (streetNameX + streetNameWidth) - (utils.stringWidth(g2, propertyName) / 2),
                streetNameY + streetNameHeight);
    }

    protected void drawOuterBorder(@NotNull Graphics2D g2) {
        g2.setStroke(streetInfoStroke);
        g2.setColor(Color.black);
        g2.drawRoundRect(outerRectX, outerRectY, outerRectWidth, outerRectHeight, outerRectArc, outerRectArc);

        g2.setColor(innerStreetInfoColor);
        g2.fillRoundRect(outerRectX, outerRectY, outerRectWidth, outerRectHeight, outerRectArc, outerRectArc);
    }


    protected void drawBuyPrice(@NotNull Graphics2D g2) {
        g2.setFont(textFont);

        g2.drawString("Grundstückswert", infoTextX, (int) (infoTextY + 75 * scaleFactor));
        g2.drawString(String.format("%d€", price),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", price)),
                (int) (infoTextCostY + 75 * scaleFactor));
    }
    protected int drawLongText(Graphics2D g2,String text, int startingIndex,int paragraph, boolean centered) {
        g2.setColor(Color.black);
        StringBuilder tmpString = new StringBuilder();
        for (String s: text.split(" ")) {
            if (s.equals("\n")) {
                drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
                startingIndex++;
                tmpString = new StringBuilder(s);
            }
            else if (utils.stringWidth(g2, tmpString + " " + s) > (innerRectWidth-10)) {
                drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
                startingIndex++;
                tmpString = new StringBuilder(s);
            } else {
                if (!tmpString.isEmpty()) {

                    tmpString.append(" ").append(s);
                } else {
                    tmpString.append(s);
                }
            }
        }
        drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
        startingIndex++;
        return startingIndex;
    }
    private void drawLine(String text, Graphics2D g2, boolean centered, int index, int paragraph) {

        int y = (int) (infoTextY + (75 + ((index + (paragraph / 2)) * 25)) * scaleFactor);

        if (centered) {
            g2.drawString(text, (outerRectWidth/2)-(utils.stringWidth(g2, text)/2)+outerRectX, y);
        } else {
            g2.drawString(text, infoTextX, y);
        }
    }
    //<editor-fold desc="Action Listeners">
    protected final ActionListener buyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!parentUI.getCurrentPlayer().buyProperty(infoProperty)) {
                parentUI.notEnoughMoneyWarning();
            } else {
                parentUI.disableInfo();
            }
        }

    };
    protected final ActionListener passListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentUI.disableInfo();
        }
    };
    //</editor-fold>

}
