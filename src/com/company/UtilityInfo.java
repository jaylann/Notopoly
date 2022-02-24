package com.company;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UtilityInfo extends PropertyInfo {
    protected UtilityInfo(UtilityCompany prop, UI pUI) {
        super(pUI, prop, true);
    }

    protected UtilityInfo(UtilityCompany prop, UI pUI, Player landed, boolean buyable) {
        super(pUI, prop, landed, buyable);
    }

    private void drawUtilityAmount(@NotNull Graphics2D g2) {
        g2.setFont(textFont);
        g2.setColor(Color.black);

        g2.drawString(String.format("Stationen in Besitz: %d",
                        ((UtilityCompany) infoProperty).getOwnedUtilityCompanies().size()),
                infoTextX, (int) (infoTextY + 290 * scaleFactor));
    }

    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);
        drawStreetName(g2);

        if (hasOwner) {
            drawDivider(g2);
            drawOwner(g2);
            drawUtilityAmount(g2);
        }

        drawBuyPrice(g2);
        drawRentInformation(g2);
        drawButtons(g2);
    }

    private void drawRentInformation(@NotNull Graphics2D g2) {
        g2.setFont(smallTextFont);
        g2.setColor(Color.black);

        if (hasOwner) {
            String firstDescription = "Als Besitzer eines Werkes ist so ist die Miete 80mal so hoch, wie Augen auf den Würfel sind";
            String secondDescription = "Als Besitzer beider Werke, Elektrizitäts- und Wasser-Werk, ist, so ist die Miete 200mal so hoch wie Augen auf den Würfeln sind";

            if (((UtilityCompany) infoProperty).getOwnedUtilityCompanies().size() < 2) {
                drawLongText(g2, firstDescription, 1, 1, false);
            } else {
                drawLongText(g2, secondDescription, 1, 1, false);
            }

        } else {
            String firstDescription = "Wenn man Besitzer eines Werkes ist so ist die Miete 80mal so hoch, wie Augen auf den Würfel sind";
            String secondDescription = "Wenn man Besitzer beider Werke, Elektrizitäts- und Wasser-Werk, ist, so ist die Miete 200mal so hoch wie Augen auf den Würfeln sind";

            int j = drawLongText(g2, firstDescription, 1, 1, false);
            drawLongText(g2, secondDescription, j, 2, false);
        }
    }
}
