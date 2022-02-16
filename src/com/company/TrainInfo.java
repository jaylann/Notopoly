package com.company;

import java.awt.*;



public class TrainInfo extends Info{
    protected TrainInfo(TrainStation prop, UI pUI) {
        super(prop, pUI);
        rent = prop.getRent();
    }

    @Override
    void draw(Graphics2D g2) {

        drawOuterBorder(g2);

        drawStreetName(g2);

        drawBuyPrice(g2);

        drawRentPrices(g2);

        drawButtons(g2);

    }

    private void drawRentPrices(Graphics2D g2) {
        g2.setColor(Color.black);
        String[] information = {"Miete", "    mit 2 Bahnhöfen", "    mit 3 Bahnhöfen",
                "    mit 4 Bahnhöfen"};

        for (int i = 1; i < information.length + 1; i++) {
            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (75 + ((i+1) * 25)) * scaleFactor));

            g2.drawString(String.format("%d€", (int) Math.pow(2,i-1) * rent),
                    infoTextCostX - utils.stringWidth(g2, String.format("%d€", (int) Math.pow(2,i-1) * rent)),
                    (int) (infoTextCostY + (75 + ((i+1) * 25)) * scaleFactor));
        }
    }

    private final int rent;
}
