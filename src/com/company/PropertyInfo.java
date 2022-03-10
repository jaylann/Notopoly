package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PropertyInfo extends Info{
    protected PropertyInfo(UI pUI, Property prop, boolean buyable) {
        super(pUI);
        infoProperty = prop;
        propertyName = infoProperty.getName();
        price = prop.getBuyPrice();
        landed = null;
        hasOwner = false;
        this.buyable=buyable;
        //Giving Buttons Functionality
        buyButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, buyListener);
        passButton = parentUI.createButton(buttonWidth, buttonHeight, passButtonX, buttonY, passListener);
    }
    protected PropertyInfo(UI pUI, Property prop, Player landed, boolean buyable) {
        super(pUI);
        infoProperty = prop;
        propertyName = infoProperty.getName();
        price = prop.getBuyPrice();

        this.landed = landed;
        this.buyable = buyable;
        //Giving Buttons Functionality
        if (!buyable) {
            if (infoProperty.getOwner() != null) {
                this.hasOwner = true;
            }
            if (landed != null) {
                if (landed.equals(infoProperty.getOwner())) {
                    System.out.println("TRUE");
                    isOwner = true;
                }
                passButtonX = (outerRectWidth/2)-(buttonWidth/2)+outerRectX;
            } else {
                buyButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, buyListener);
            }
        } else {
            buyButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, buyListener);
            hasOwner = false;
        }
        passButton = parentUI.createButton(buttonWidth, buttonHeight, passButtonX, buttonY, passListener);
    }
    protected Player landed;
    protected boolean hasOwner;
    protected boolean isOwner = false;
    protected boolean buyable;
    protected void drawOwner(@NotNull Graphics2D g2) {
        g2.setFont(textFont);
        g2.setColor(Color.black);

        if (isOwner) {
            g2.drawString("Besitzer: Du", infoTextX, (int) (infoTextY + 265 * scaleFactor));
        } else {
            g2.drawString(String.format("Besitzer: %s", landed.getName()), infoTextX, (int) (infoTextY + 265 * scaleFactor));
        }
    }
    @Override
    protected void drawButtons(@NotNull Graphics2D g2) {
        //Drawing Buttons
        g2.setFont(textFont);
        g2.setColor(buyColor);

        if (!buyable) {
            //Drawing one button in the middle
            g2.fillRoundRect(passButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);
        } else {
            //Drawing two buttons. One for buying one for passing
            g2.fillRoundRect(buyButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);
            g2.setColor(continueColor);
            g2.fillRoundRect(passButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);
        }
        //Drawing text on Buttons
        g2.setColor(Color.white);

        if (!buyable) {
            //Drawing one button in the middle
            g2.drawString("Weiter",
                    (int) (passButtonX + (utils.stringWidth(g2,"Weiter")/2)+(5*scaleFactor)),
                    (int) (buttonY + 37 * scaleFactor));
        } else {
            //Drawing two buttons. One for buying one for passing
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
    }

    @Override
    public ArrayList<JButton> getButtons() {
        if (buyable) {
            return new ArrayList<>(Arrays.asList(buyButton, passButton));
        } else {
            return new ArrayList<>(List.of(passButton));
        }
    }

    protected void drawDivider(@NotNull Graphics2D g2) {
        g2.setStroke(cutStroke);
        g2.drawRect(infoTextX, (int) (infoTextY + 235 * scaleFactor), (int) (275 * scaleFactor), 1);
    }
}
