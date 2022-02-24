package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionInfo extends Info{
    private final String headText;
    private final String bodyText;
    private final JButton acceptButton;
    private final JButton choiceButton;
    private final CommunityAction.Action action;
    protected ActionInfo(SpecialField caller, UI pUI, CommunityAction.Action action) {
        super(pUI);
        propertyName = caller.getName();
        infoProperty = null;
        buyButton = null;
        passButton = null;
        this.action = action;
        this.headText = action.getTitle();
        this.bodyText = action.getInfoText();
        ActionListener acceptListener = action.getAcceptListener();
        if (this.action.getClass() == CommunityAction.PayOrChance.class) {
            ActionListener choiceListener = action.getChoiceListener();
            acceptButton = parentUI.createButton(buttonWidth, buttonHeight, buyButtonX, buttonY, acceptListener);
            choiceButton = parentUI.createButton(buttonWidth, buttonHeight, passButtonX, buttonY, choiceListener);

        } else {

            acceptButton = parentUI.createButton(buttonWidth, buttonHeight, (outerRectWidth/2)-(buttonWidth/2)+outerRectX, buttonY, acceptListener);
            choiceButton = null;
        }

    }

    @Override
    public ArrayList<JButton> getButtons() {
        if (action.hasChoice()) {
            return new ArrayList<>(Arrays.asList(acceptButton, choiceButton));
        } else {
            return new ArrayList<>(Arrays.asList(acceptButton));
        }
    }


    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);
        drawStreetName(g2);
        g2.setFont(textFont);
        int j = drawLongText(g2,headText,0,1, true);
        drawLongText(g2,bodyText,j,2, true);
        drawButtons(g2);
    }

    @Override
    protected void drawButtons(@NotNull Graphics2D g2) {
        //Drawing Buttons
        g2.setFont(textFont);
        g2.setColor(buyColor);
        if (this.action.getClass() == CommunityAction.PayOrChance.class) {
            g2.fillRoundRect(buyButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

            g2.setColor(continueColor);
            g2.fillRoundRect(passButtonX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);

            //Drawing text on Buttons
            g2.setColor(Color.white);
            g2.drawString("Zahle",
                    (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Zahle") / 2),
                    (int) (buttonY + 25 * scaleFactor));

            g2.drawString("200€",
                    (buyButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "200€") / 2),
                    (int) (buttonY + 50 * scaleFactor));

            g2.drawString("Ereignis-",
                    (passButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Ereignis-") / 2),
                    (int) (buttonY + 25 * scaleFactor));

            g2.drawString("Karte",
                    (passButtonX + (buttonWidth / 2)) - (utils.stringWidth(g2, "Karte") / 2),
                    (int) (buttonY + 50 * scaleFactor));
        } else {
            g2.fillRoundRect((outerRectWidth/2)-(buttonWidth/2)+outerRectX, buttonY, buttonWidth, buttonHeight, buttonArc, buttonArc);
        g2.setColor(Color.white);
        g2.drawString("Weiter",
                (int) ((outerRectWidth/2)-(buttonWidth/2)+outerRectX + (utils.stringWidth(g2,"Weiter")/2)+(5*scaleFactor)),
                (int) (buttonY + 37 * scaleFactor));
        }

    }

}
