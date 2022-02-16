package com.company;

import java.awt.*;

public class UtilityInfo extends Info{
    protected UtilityInfo(UtilityCompany prop, UI pUI) {
        super(prop, pUI);
    }

    @Override
    void draw(Graphics2D g2) {
        drawOuterBorder(g2);

        drawStreetName(g2);

        drawBuyPrice(g2);

        drawRentInformation(g2);

        drawButtons(g2);
    }
    private void drawRentInformation(Graphics2D g2) {
        g2.setFont(smallTextFont);
        g2.setColor(Color.black);

        String firstDescription = "Wenn man Besitzer eines Werkes ist so ist die Miete 80mal so hoch, wie Augen auf den Würfel sind";
        String secondDescription = "Wenn man Besitzer beider Werke, Elektrizitäts- und Wasser-Werk, ist, so ist die Miete 200mal so hoch wie Augen auf den Würfeln sind";
        int j = drawLongText(g2,firstDescription,1,1);
        drawLongText(g2, secondDescription, j,2);

    }


}
