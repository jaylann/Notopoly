package com.company;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;


public class StreetInfo extends PropertyInfo {

    //<editor-fold desc="Property Values">
    protected final int currentRent;
    private final Color streetColor;
    private final int housePrice;
    private final ArrayList<Integer> priceList;
    //</editor-fold>

    public StreetInfo(@NotNull Street street, @NotNull UI pUI) {
        super(pUI, street, true);

        //Initializing Variables
        //<editor-fold desc="Property Values">
        priceList = street.getPriceList();
        housePrice = street.getHousePrice();
        streetColor = street.getColor();
        currentRent = 0;
        //</editor-fold>
    }

    public StreetInfo(@NotNull Street street, @NotNull UI pUI, Player landed, boolean buyable) {
        super(pUI, street, landed, buyable);

        //Initializing Variables
        //<editor-fold desc="Property Values">
        priceList = street.getPriceList();
        housePrice = street.getHousePrice();
        streetColor = street.getColor();
        currentRent = street.getRent();
        //</editor-fold>
    }

    protected void drawCurrentRent(@NotNull Graphics2D g2) {
        g2.setFont(textFont);

        g2.drawString("Aktuelle Miete", infoTextX, (int) (infoTextY + 75 * scaleFactor));

        g2.drawString(String.format("%d€", currentRent),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", currentRent)),
                (int) (infoTextCostY + 75 * scaleFactor));
    }

    private void drawHouseAmount(@NotNull Graphics2D g2) {
        g2.setFont(textFont);
        g2.setColor(Color.black);

        g2.drawString(String.format("Gebaute Häuser: %d", ((Street) infoProperty).getHouses()),
                infoTextX, (int) (infoTextY + 290 * scaleFactor));
    }

    public void draw(Graphics2D g2) {
        drawOuterBorder(g2);
        drawInnerBorder(g2);
        drawStreetName(g2);

        if (hasOwner) {
            drawCurrentRent(g2);
            drawOwner(g2);
            drawHouseAmount(g2);
        } else {
            drawBuyPrice(g2);
            drawHousePrices(g2);
        }

        drawRentPrices(g2);
        drawDivider(g2);
        drawButtons(g2);
    }

    private void drawHousePrices(@NotNull Graphics2D g2) {
        g2.setFont(textFont);
        g2.setColor(Color.black);
        g2.drawString("1 Haus kostet", infoTextX, (int) (infoTextY + 265 * scaleFactor));

        g2.drawString(String.format("%d€", housePrice),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", housePrice)),
                (int) (infoTextY + 265 * scaleFactor));

        g2.drawString("1 Hotel - 4 Häuser u.", infoTextX, (int) (infoTextY + 290 * scaleFactor));
        g2.drawString(String.format("%d€", housePrice),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", housePrice)),
                (int) (infoTextY + 290 * scaleFactor));
    }

    private void drawRentPrices(@NotNull Graphics2D g2) {
        String[] information = {"Miete Grundstück allein", "    mit 1 Haus", "         2 Häusern",
                "         3 Häusern", "         4 Häusern", "         1 Hotel"};

        for (int i = 1; i < information.length + 1; i++) {
            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (75 + (i * 25)) * scaleFactor));

            g2.drawString(String.format("%d€", priceList.get(i - 1)),
                    infoTextCostX - utils.stringWidth(g2, String.format("%d€", priceList.get(i - 1))),
                    (int) (infoTextCostY + (75 + (i * 25)) * scaleFactor));
        }
    }

    private void drawInnerBorder(@NotNull Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(streetInfoInnerStroke);
        g2.drawRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);

        g2.setColor(streetColor);
        g2.fillRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);
    }


}
