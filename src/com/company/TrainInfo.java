package com.company;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TrainInfo extends PropertyInfo {
    private final int currentRent;
    private final int rent;

    protected TrainInfo(TrainStation prop, UI pUI) {
        super(pUI, prop, true);

        rent = prop.getRent();
        currentRent = 0;
    }

    protected TrainInfo(TrainStation prop, UI pUI, Player landed, boolean buyable) {
        super(pUI, prop, landed, buyable);

        rent = prop.getRent();

        if (hasOwner) {
            currentRent = prop.getCurrentRent();
        } else {
            currentRent = 0;
        }
    }

    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);
        drawStreetName(g2);

        if (hasOwner) {
            drawDivider(g2);
            drawCurrentRent(g2);
            drawStationAmount(g2);
            drawOwner(g2);
        } else {
            drawBuyPrice(g2);
        }

        drawRentPrices(g2);
        drawButtons(g2);

    }

    private void drawStationAmount(@NotNull Graphics2D g2) {
        g2.setFont(textFont);
        g2.setColor(Color.black);

        g2.drawString(String.format("Stationen in Besitz: %d", ((TrainStation) infoProperty).getOwnedTrainStations().size()), infoTextX, (int) (infoTextY + 290 * scaleFactor));
    }

    protected void drawCurrentRent(@NotNull Graphics2D g2) {
        g2.setFont(textFont);

        g2.drawString("Aktuelle Miete", infoTextX, (int) (infoTextY + 75 * scaleFactor));

        g2.drawString(String.format("%d€", currentRent),
                infoTextCostX - utils.stringWidth(g2, String.format("%d€", currentRent)),
                (int) (infoTextCostY + 75 * scaleFactor));
    }

    private void drawRentPrices(@NotNull Graphics2D g2) {
        g2.setColor(Color.black);
        String[] information = {"Miete", "    mit 2 Bahnhöfen", "    mit 3 Bahnhöfen",
                "    mit 4 Bahnhöfen"};

        for (int i = 1; i < information.length + 1; i++) {
            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (75 + ((i + 1) * 25)) * scaleFactor));

            g2.drawString(String.format("%d€", (int) Math.pow(2, i - 1) * rent),
                    infoTextCostX - utils.stringWidth(g2, String.format("%d€", (int) Math.pow(2, i - 1) * rent)),
                    (int) (infoTextCostY + (75 + ((i + 1) * 25)) * scaleFactor));
        }
    }
}
