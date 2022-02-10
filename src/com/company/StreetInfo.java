package com.company;

import java.awt.*;
import java.util.ArrayList;

public class StreetInfo {

    private Property infoProperty;
    private String propertyName;
    private final ArrayList<Integer> priceList;
    private final int price;
    private final int housePrice;
    private final UI parentUI;
    private final double scaleFactor;

    public StreetInfo(Street street, UI pUI) {
        infoProperty = street;
        propertyName = infoProperty.getName();
        priceList = street.getPriceList();
        price = street.getBuyPrice();
        housePrice = street.getHousePrice();
        streetColor = street.getColor();

        parentUI = pUI;
        scaleFactor= parentUI.getScaleFactor();

        infoRectX = (int) (600*scaleFactor);
        infoRectY = (int) (350*scaleFactor);
        infoRectHeight = (int) (455*scaleFactor);
        infoRectWidth = (int) (325*scaleFactor);
        infoRectArc = (int) (50*scaleFactor);

        streetRectX = (int) (infoRectX + (25*scaleFactor));
        streetRectY = (int) (infoRectY + (25*scaleFactor));
        streetRectWidth = (int) (infoRectWidth - (50*scaleFactor));
        streetRectHeight = (int) (75*scaleFactor);
        streetRectArc = (int) (25*scaleFactor);

        textX = streetRectX;
        textY = (int) (streetRectY+12.5*scaleFactor);
        textWidth = streetRectWidth/2;
        textHeight = (int) (streetRectHeight/2.0);

        infoTextX = textX;
        infoTextY = (int) ((375+(75/2)+10)*scaleFactor);
        infoTextCostX = (int) (900*scaleFactor);
        infoTextCostY = infoTextY;
    }

    private final Font headFont = new Font("Roboto", Font.PLAIN, (int) (35));
    private final Font textFont = new Font("Roboto", Font.PLAIN, (int) (20));
    private final Color outerStreetInfoColor = new Color(0, 112, 56,150);
    private final Color innerStreetInfoColor = new Color(229, 234, 229,250);
    private final Color streetColor;
    private final Stroke streetInfoStroke = new BasicStroke(10);
    private final Stroke streetInfoInnerStroke = new BasicStroke(5);
    private final Stroke cutStroke = new BasicStroke(1);
    private final Color buyColor = new Color(12, 156, 13);
    private final Color continueColor = new Color(217, 53, 53);

    private final int infoRectX;
    private final int infoRectY;
    private final int infoRectWidth;
    private final int infoRectHeight;
    private final int infoRectArc;

    private final int streetRectX;
    private final int streetRectY;
    private final int streetRectWidth;
    private final int streetRectHeight;
    private final int streetRectArc;

    private final int textX;
    private final int textY;
    private final int textWidth;
    private final int textHeight;

    private final int infoTextX;
    private final int infoTextY;
    private final int infoTextCostX;
    private final int infoTextCostY;

    public void draw(Graphics2D g2) {

        g2.setStroke(streetInfoStroke);
        g2.setColor(Color.black);
        g2.drawRoundRect(infoRectX,infoRectY,infoRectWidth,infoRectHeight,infoRectArc,infoRectArc);
        g2.setColor(innerStreetInfoColor);
        g2.fillRoundRect(infoRectX,infoRectY,infoRectWidth,infoRectHeight,infoRectArc,infoRectArc);

        g2.setColor(streetColor);
        g2.fillRoundRect(streetRectX,streetRectY,streetRectWidth,streetRectHeight,streetRectArc,streetRectArc);
        g2.setColor(Color.black);
        g2.setStroke(streetInfoInnerStroke);
        g2.drawRoundRect(streetRectX,streetRectY,streetRectWidth,streetRectHeight,streetRectArc,streetRectArc);

        g2.setFont(headFont);
        g2.drawString(propertyName, (textX+textWidth)-(g2.getFontMetrics().stringWidth(propertyName)/2), textY+textHeight);

        g2.setFont(textFont);

        g2.drawString("Grundstückswert",infoTextX, (int) (infoTextY+75*scaleFactor));
        g2.drawString(String.format("%d€", price),infoTextCostX-g2.getFontMetrics().stringWidth(String.format("%d€", price)), (int) (infoTextCostY+75*scaleFactor));

        String[] information = {"Miete Grundstück allein", "    mit 1 Haus", "         2 Häusern", "         3 Häusern", "         4 Häusern"};
        System.out.println(priceList);
        for (int i=1; i < information.length+1; i++) {
            g2.drawString(information[i-1],infoTextX, (int) (infoTextY+(75+(i*25))*scaleFactor));
            g2.drawString(String.format("%d€",  priceList.get(i-1)),infoTextCostX-g2.getFontMetrics().stringWidth(String.format("%d€", priceList.get(i-1))), (int) (infoTextCostY+(75+(i*25))*scaleFactor));
        }

        g2.setStroke(cutStroke);
        g2.drawRect(625,375+(75/2)+10+215,275,1);
        g2.drawString("1 Haus kostet",625,375+(75/2)+10+250);
        g2.drawString(String.format("%d€", housePrice),900-g2.getFontMetrics().stringWidth(String.format("%d€", housePrice)), 375+(75/2)+10+250);
        g2.drawString("1 Hotel - 4 Häuser u.",625,375+(75/2)+10+275);
        g2.drawString(String.format("%d€", housePrice),900-g2.getFontMetrics().stringWidth(String.format("%d€", housePrice)), 375+(75/2)+10+275);
        g2.setColor(buyColor);
        g2.fillRoundRect(625,375+(75/2)+10+300,125,60,15,15);
        g2.setColor(continueColor);
        g2.fillRoundRect(775,375+(75/2)+10+300,125,60,15,15);
        g2.setColor(Color.white);
        g2.drawString("Kaufen",(625+(125/2))-(g2.getFontMetrics().stringWidth("Kaufen")/2),375+(75/2)+10+325);
        g2.drawString(String.format("%d€", price),(625+(125/2))-(g2.getFontMetrics().stringWidth(String.format("%d€", price))/2),375+(75/2)+10+350);
        g2.drawString("Weiter",(775+(125/2))-(g2.getFontMetrics().stringWidth("Weiter")/2),375+(75/2)+10+337);
    }

}
