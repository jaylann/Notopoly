package com.company;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrisonInfo extends Info {
    //</editor-fold>
    protected final ActionListener passListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentUI.disableInfo();
        }
    };
    private final Board bp;
    private final int price;
    //Giving Buttons Functionality
    ActionListener buyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(price);
            System.out.println(bp.getPrisonField().getPrice());
            if (parentUI.getCurrentPlayer().getTimeInPrison() > 0) {
                if (parentUI.getCurrentPlayer().hasPrisonFree()) {
                    parentUI.getCurrentPlayer().removePrisonFree();
                    parentUI.disableInfo();
                    parentUI.getCurrentPlayer().setPrison(false);
                    if (parentUI.getCurrentPlayer().getRecentRoll() != 0) {
                        parentUI.getCurrentPlayer().move(parentUI.getCurrentPlayer().getRecentRoll(), bp);
                    }
                }
                else if (!parentUI.getCurrentPlayer().payMiddle(bp.getPrisonField().getPrice(), bp, false)) {
                    parentUI.notEnoughMoneyWarning();
                } else {
                    parentUI.disableInfo();
                    parentUI.getCurrentPlayer().setPrison(false);
                    if (parentUI.getCurrentPlayer().getRecentRoll() != 0) {
                        parentUI.getCurrentPlayer().move(parentUI.getCurrentPlayer().getRecentRoll(), bp);
                    }
                }
            } else {
                parentUI.prisonWarning();
            }

        }
    };

    protected PrisonInfo(SpecialField prop, UI pUI, Board bp, int price) {
        super(pUI);
        propertyName = prop.getName();
        this.bp = bp;
        this.price = price;

        buyButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, buyListener);
        passButton = parentUI.createButton(buttonWidth, buttonHeight, passButtonX, buttonY, passListener);
    }

    //<editor-fold desc="Action Listeners">

    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);

        drawStreetName(g2);

        drawButtons(g2);

        drawLongText(g2, "Sie sind im Gefängnis gelandet. Sie können sich entweder direkt freikaufen, 3 Runden warten und dann freikaufen, eine 'Gefängnisfrei' Karte verwenden oder einen Pasch würfeln um aus dem Gefängnis zu entkommen", 0, 1, false);
    }

    @Override
    protected void drawButtons(@NotNull Graphics2D g2) {
        //Drawing Buttons
        g2.setFont(textFont);
        g2.setColor(buyColor);
        g2.fillRoundRect(buyButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

        g2.setColor(continueColor);
        g2.fillRoundRect(passButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

        //Drawing text on Buttons
        g2.setColor(Color.white);
        g2.drawString("Freikauf",
                (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Freikauf") / 2),
                (int) (buttonY + 25 * scaleFactor));

        g2.drawString(String.format("%d€", price),
                (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, String.format("%d€", price)) / 2),
                (int) (buttonY + 50 * scaleFactor));

        g2.drawString("Weiter",
                (passButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Weiter") / 2),
                (int) (buttonY + 37 * scaleFactor));
    }
}
