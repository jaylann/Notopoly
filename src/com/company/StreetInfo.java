package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class StreetInfo {

    //<editor-fold desc="Property Values">
    private final Property infoProperty;
    private final String propertyName;
    private final ArrayList<Integer> priceList;
    private final Color streetColor;
    private final int price;
    private final int housePrice;
    //</editor-fold>
    //<editor-fold desc="Dependencies">
    private final UI parentUI;
    private final double scaleFactor;
    //</editor-fold>
    //<editor-fold desc="Internal">
    private boolean notEnoughMoney = false;
    private int noMoneyDrawCount = 0;

    //</editor-fold>
    //<editor-fold desc="Fonts">
    private final Font headFont;
    private final Font textFont;
    private final Font semiHeadFont;
    //</editor-fold>
    //<editor-fold desc="Colors">
    private final Color innerStreetInfoColor = new Color(229, 234, 229, 250);
    private final Color buyColor = new Color(12, 156, 13);
    private final Color continueColor = new Color(217, 53, 53);
    //</editor-fold>
    //<editor-fold desc="Strokes">
    private final Stroke streetInfoStroke;
    private final Stroke streetInfoInnerStroke;
    private final Stroke cutStroke;
    //</editor-fold>
    //<editor-fold desc="Outer Rectangle Values">
    private final int outerRectX;
    private final int outerRectY;
    private final int outerRectWidth;
    private final int outerRectHeight;
    private final int outerRectArc;
    //</editor-fold>
    //<editor-fold desc="Inner Rectangle Values">
    private final int innerRectX;
    private final int innerRectY;
    private final int innerRectWidth;
    private final int innerRectHeight;
    private final int innerRectArc;
    //</editor-fold>
    //<editor-fold desc="Street Name Values">
    private final int streetNameX;
    private final int streetNameY;
    private final int streetNameWidth;
    private final int streetNameHeight;
    //</editor-fold>
    //<editor-fold desc="Info Text Values">
    private final int infoTextX;
    private final int infoTextY;
    private final int infoTextCostX;
    private final int infoTextCostY;
    //</editor-fold>
    //<editor-fold desc="Button Values">
    private final int buyButtonX;
    private final int passButtonX;
    private final int buttonY;
    private final int buttonWidth;
    private final int buttonHeight;
    private final int buttonArc;
    private final JButton buyButton;
    private final JButton passButton;

    //</editor-fold>
    //<editor-fold desc="Action Listeners">
    private final ActionListener buyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!parentUI.getCurrentPlayer().buyProperty(infoProperty.getBuyPrice(), infoProperty)) {
                noMoney();
            } else {
                parentUI.disableStreetInfo();
            }
        }
    };
    private final ActionListener passListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentUI.disableStreetInfo();
        }
    };
    //</editor-fold>

    public StreetInfo(@NotNull Street street, @NotNull UI pUI) {
        //Initializing Variables
        //<editor-fold desc="Property Values">
        infoProperty = street;
        propertyName = infoProperty.getName();
        priceList = street.getPriceList();
        price = street.getBuyPrice();
        housePrice = street.getHousePrice();
        streetColor = street.getColor();
        //</editor-fold>
        //<editor-fold desc="Dependencies">
        parentUI = pUI;
        scaleFactor = parentUI.getScaleFactor();
        //</editor-fold>
        //<editor-fold desc="Fonts">
        headFont = new Font("Roboto", Font.PLAIN, (int) (35 * scaleFactor));
        textFont = new Font("Roboto", Font.PLAIN, (int) (20 * scaleFactor));
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
        infoTextY = (int) ((375 + (75 / 2) + 10) * scaleFactor);
        infoTextCostX = (int) (900 * scaleFactor);
        infoTextCostY = infoTextY;
        //</editor-fold>
        //<editor-fold desc="Button Values">
        buttonY = (int) (infoTextY + 300 * scaleFactor);
        passButtonX = (int) (775 * scaleFactor);
        buyButtonX = (int) (625 * scaleFactor);
        buttonWidth = (int) (125 * scaleFactor);
        buttonHeight = (int) (60 * scaleFactor);
        buttonArc = (int) (15 * scaleFactor);
        //</editor-fold>

        //Giving Buttons Functionality
        buyButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, buyListener);
        passButton = parentUI.createButton(buttonWidth, buttonHeight, passButtonX, buttonY, passListener);
    }

    public void draw(Graphics2D g2) {

        drawOuterBorder(g2);

        drawInnerBorder(g2);

        drawStreetName(g2);

        drawBuyPrice(g2);

        drawRentPrices(g2);

        drawDivider(g2);

        drawHousePrices(g2);

        drawButtons(g2);

        if (notEnoughMoney) {
            moneyWarning(g2);
        }
    }

    private void noMoney() {
        notEnoughMoney = true;
        noMoneyDrawCount = 0;
    }

    public ArrayList<JButton> getButtons() {
        return new ArrayList<>(Arrays.asList(buyButton, passButton));
    }

    private void moneyWarning(@NotNull Graphics2D g2) {
        //Increasing transparency with each iteration
        g2.setColor(new Color(255, 0, 0, 255 - noMoneyDrawCount));

        g2.setFont(headFont);
        g2.drawString("NICHT GENÜGEND GELD", (parentUI.getWidth() / 2) - (utils.stringWidth(g2, "NICHT GENÜGEND GELD") / 2), (parentUI.getHeight()) / 2);

        noMoneyDrawCount += 1;
        if (noMoneyDrawCount > 255) {
            notEnoughMoney = false;
            noMoneyDrawCount = 0;
        }
    }

    private void drawButtons(@NotNull Graphics2D g2) {
        //Drawing Buttons
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

    private void drawHousePrices(@NotNull Graphics2D g2) {
        g2.drawString("1 Haus kostet", infoTextX, (int) (infoTextY + 250 * scaleFactor));

        g2.drawString(String.format("%d€", housePrice),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", housePrice)),
                (int) (infoTextY + 250 * scaleFactor));

        g2.drawString("1 Hotel - 4 Häuser u.", infoTextX, (int) (infoTextY + 275 * scaleFactor));
        g2.drawString(String.format("%d€", housePrice),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", housePrice)),
                (int) (infoTextY + 275 * scaleFactor));
    }

    private void drawRentPrices(@NotNull Graphics2D g2) {
        String[] information = {"Miete Grundstück allein", "    mit 1 Haus", "         2 Häusern",
                "         3 Häusern", "         4 Häusern"};

        for (int i = 1; i < information.length + 1; i++) {
            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (75 + (i * 25)) * scaleFactor));

            g2.drawString(String.format("%d€", priceList.get(i - 1)),
                    infoTextCostX - utils.stringWidth(g2, String.format("%d€", priceList.get(i - 1))),
                    (int) (infoTextCostY + (75 + (i * 25)) * scaleFactor));
        }
    }

    private void drawBuyPrice(@NotNull Graphics2D g2) {
        g2.setFont(textFont);

        g2.drawString("Grundstückswert", infoTextX, (int) (infoTextY + 75 * scaleFactor));
        g2.drawString(String.format("%d€", price),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", price)),
                (int) (infoTextCostY + 75 * scaleFactor));
    }

    private void drawStreetName(@NotNull Graphics2D g2) {
        g2.setFont(headFont);
        g2.setColor(Color.black);
        if (utils.stringWidth(g2, propertyName) > innerRectWidth) {
            g2.setFont(semiHeadFont);
        }
        g2.drawString(propertyName,
                (streetNameX + streetNameWidth) - (utils.stringWidth(g2, propertyName) / 2),
                streetNameY + streetNameHeight);
    }

    private void drawInnerBorder(@NotNull Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(streetInfoInnerStroke);
        g2.drawRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);

        g2.setColor(streetColor);
        g2.fillRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);
    }

    private void drawOuterBorder(@NotNull Graphics2D g2) {
        g2.setStroke(streetInfoStroke);
        g2.setColor(Color.black);
        g2.drawRoundRect(outerRectX, outerRectY, outerRectWidth, outerRectHeight, outerRectArc, outerRectArc);

        g2.setColor(innerStreetInfoColor);
        g2.fillRoundRect(outerRectX, outerRectY, outerRectWidth, outerRectHeight, outerRectArc, outerRectArc);
    }

    private void drawDivider(@NotNull Graphics2D g2) {
        g2.setStroke(cutStroke);
        g2.drawRect(infoTextX, (int) (infoTextY + 215 * scaleFactor), (int) (275 * scaleFactor), 1);
    }
}
