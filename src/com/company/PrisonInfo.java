package com.company;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrisonInfo extends Info{
    private final Board bp;
    private final int price;
    protected PrisonInfo(SpecialField prop, UI pUI, Board bp, int price) {
        super(prop, pUI);
        this.bp = bp;
        this.price = price;
    }

    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);

        drawStreetName(g2);

        drawButtons(g2);

        drawLongText(g2,"Sie sind im Gefängnis gelandet. Sie können sich entweder direkt freikaufen, 3 Runden warten und dann freikaufen, eine 'Gefängnisfrei' Karte verwenden oder einen Pasch würfeln um aus dem Gefängnis zu entkommen",0,1);
    }

    //<editor-fold desc="Action Listeners">
    protected final ActionListener buyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!parentUI.getCurrentPlayer().payMiddle(bp.getPrisonField().getPrice(), bp, false)) {
                parentUI.notEnoughMoneyWarning();
            } else {
                parentUI.disableInfo();
                parentUI.getCurrentPlayer().setPrison(false);
            }
        }
    };
    //</editor-fold>


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
