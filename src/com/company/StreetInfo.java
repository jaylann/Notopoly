package com.company;

import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.util.ArrayList;


public class StreetInfo extends Info{

    //<editor-fold desc="Property Values">

    private final Color streetColor;
    private final int housePrice;
    private final ArrayList<Integer> priceList;
    //</editor-fold>

    public StreetInfo(@NotNull Street street, @NotNull UI pUI) {
        super(street, pUI);
        //Initializing Variables
        //<editor-fold desc="Property Values">
        priceList = street.getPriceList();
        housePrice = street.getHousePrice();
        streetColor = street.getColor();
        //</editor-fold>
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


    private void drawInnerBorder(@NotNull Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(streetInfoInnerStroke);
        g2.drawRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);

        g2.setColor(streetColor);
        g2.fillRoundRect(innerRectX, innerRectY, innerRectWidth, innerRectHeight, innerRectArc, innerRectArc);
    }

    private void drawDivider(@NotNull Graphics2D g2) {
        g2.setStroke(cutStroke);
        g2.drawRect(infoTextX, (int) (infoTextY + 215 * scaleFactor), (int) (275 * scaleFactor), 1);
    }
}
