package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

public class Inventory {

    private final Player player;
    private final Board bp;
    private final UI parentUI;
    private int page = 0;
    private int pages;

    //private final JButton nextPageButton;
    //private final JButton lastPageButton;
    //private final JButton closePageButton;
    //private final JButton acceptPageButton;
    //private final JButton closeInventoryButton;

    private ArrayList<JButton> propertyButtons = new ArrayList<>();
    //<editor-fold desc="Action Listeners">
    private final ActionListener backListener = e -> {
        if (page > 0) {
            page--;
        }
    };

    private final ActionListener nextListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (pages > (page + 1)) {
                page++;
            }
        }
    };

    private final ArrayList<Player> players = new ArrayList<>();
    private final ActionListener closeInventoryListener = e -> {
        end();
    };

    private ArrayList<JButton> streetButtons = new ArrayList<>();

    public ArrayList<JButton> getButtons() {
        //return new ArrayList<>(Arrays.asList(nextPageButton, lastPageButton,closePageButton,closeInventoryButton))
        return new ArrayList<>();
    }

    private void end() {
        parentUI.closeInventory();
    }

    public Inventory(Player p, Board bp, UI parentUI) {
        this.player = p;
        this.bp = bp;
        this.parentUI = parentUI;

        //for (int i = 0; i< player.getProperties().size(); i++) {
            //parentUI.createButton(100,50)
        //}
        scaleFactor = parentUI.getScaleFactor();
        nameFont =  new Font("Roboto", Font.PLAIN, (int) (35 * scaleFactor));
        smallNameFont =  new Font("Roboto", Font.PLAIN, (int) (30 * scaleFactor));
        textFont = new Font("Roboto", Font.PLAIN, (int) (25 * scaleFactor));
        smallTextFont = new Font("Roboto", Font.PLAIN, (int) (20 * scaleFactor));
    }
    private final Font smallTextFont;
    private final Stroke outerStroke = new BasicStroke(10);
    private final Color menuColor = new Color(220,220,220);
    private final double scaleFactor;
    public void draw(Graphics2D g2) {



        if (showDetails) {
            g2.setStroke(outerStroke);
            g2.setColor(Color.black);
            g2.drawRoundRect(150,150,450,700,50,50);
            g2.setColor(menuColor);
            g2.fillRoundRect(150,150,450,700,50,50);
            drawDetails(g2, detailItem);
        } else {
            g2.setStroke(outerStroke);
            g2.setColor(Color.black);
            g2.drawRoundRect(90,90,900,900,50,50);
            g2.setColor(menuColor);
            g2.fillRoundRect(90,90,900,900,50,50);
            drawProperties(g2);
        }
    }

    private final Stroke outerCardStroke = new BasicStroke(8);
    private final Stroke buttonStroke = new BasicStroke(4);

    private final Font nameFont;
    private final Font smallNameFont;

    private void drawProperties(Graphics2D g2) {
        Hashtable<Integer, ArrayList<Property>> sortedPropertyList = player.getPropertiesSorted();

        Enumeration<Integer> e = sortedPropertyList.keys();

        // Iterating through the Hashtable
        // object

        // Checking for next element in Hashtable object
        // with the help of hasMoreElements() method
        g2.setColor(Color.black);
        g2.setFont(nameFont);
        int i=0;
        while (e.hasMoreElements()) {
            System.out.println("REE");
            // Getting the key of a particular entry
            int key = e.nextElement();

            // Print and display the Rank and Name
            ArrayList<Property> sameIndexList = sortedPropertyList.get(key);
            for (int j = 0; j < sameIndexList.size(); j++) {
                System.out.println("Actual Ree");
                g2.setStroke(outerCardStroke);
                int x = 130+(285*(i%3));
                int y = 140+((i/3)*100);
                int width = 250;
                int height = 70;
                drawTitle(g2, sameIndexList.get(j), x, y, width, height);
                int finalI = i;
                ActionListener detailListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showDetails(finalI);
                    }
                };

                streetButtons.add(parentUI.createButton(width,height,x,y,detailListener));
                propertyList.add(sameIndexList.get(j));
                i++;

            }
        }
    }
    private ArrayList<Property> propertyList = new ArrayList<>();
    private void drawTitle(Graphics2D g2,Property prop, int x, int y, int width, int height) {
        g2.setFont(nameFont);
        g2.setStroke(outerCardStroke);
        g2.setColor(Color.black);
        g2.drawRoundRect(x,y,width,height,15,15);
        if (prop instanceof Street) {
            g2.setColor(((Street) prop).getColor());
        } else {
            g2.setColor(Color.white);
        }
        g2.fillRoundRect(x,y,width,height,15,15);
        g2.setColor(Color.black);
        if (utils.stringWidth(g2, prop.getName()) > (width-20)) {
            g2.setFont(utils.getFittingFont(g2,prop.getName(), nameFont, width-20));
        }
        g2.drawString(prop.getName(),  (x+width/2)-(utils.stringWidth(g2,prop.getName())/2), (int) (y+(height/1.5)));
    }

    private void drawDetails(Graphics2D g2, int index) {
        int x = 150+225-125;
        int y = 175;
        int width = 250;
        int height = 70;
        parentUI.disableButtons(streetButtons);
        drawTitle(g2,propertyList.get(index), x,y,width,height);
        if (propertyList.get(index) instanceof Street) {

            drawHousePrices(g2, (Street) propertyList.get(index));
            drawRentPrices(g2, (Street) propertyList.get(index));
            drawButtons(g2, (Street) propertyList.get(index));
            drawDivider(g2, 175,670);
            drawDivider(g2, 175,590);
        }
    }
    private final Stroke divideStroke = new BasicStroke(1);

    protected void drawDivider(@NotNull Graphics2D g2, int x, int y) {
        g2.setColor(Color.black);
        g2.setStroke(divideStroke);
        g2.drawRect(x, y, (int) (400 * scaleFactor), 1);
    }


    protected final Color buyColor = new Color(12, 156, 13);
    protected final Color sellColor = new Color(217, 53, 53);
    protected final Color warningColor = new Color(209, 82, 15);

    private final void drawButtons(Graphics2D g2, Street street) {
        drawButton(g2,infoTextX,690,120,65,10,buttonStroke,buyColor);
        drawStringAtCenter(g2,"Haus Kaufen", infoTextX+60, 690+25, smallTextFont);
        drawStringAtCenter(g2,String.format("%d €", street.getHousePrice()), infoTextX+60, 690+55, smallTextFont);

        drawButton(g2,infoTextX+140,690,120,65,10,buttonStroke,warningColor);
        drawStringAtCenter(g2,"Handeln", infoTextX+200, 690+40, textFont);

        drawButton(g2,infoTextX+280,690,120,65,10,buttonStroke,buyColor);
        drawStringAtCenter(g2,"Hypothek", infoTextX+340, 690+25, smallTextFont);
        drawStringAtCenter(g2,"Auflösen", infoTextX+340, 690+55, smallTextFont);

        drawButton(g2,infoTextX,770,120,65,10,buttonStroke,sellColor);
        drawStringAtCenter(g2,"Verkaufen", infoTextX+60, 770+25, smallTextFont);
        drawStringAtCenter(g2,String.format("%d €", street.getHousePrice()/2), infoTextX+60, 770+55, smallTextFont);
        drawButton(g2,infoTextX+140,770,120,65,10,buttonStroke,sellColor);
        drawStringAtCenter(g2,"Zurück", infoTextX+200, 770+40, textFont);
        drawButton(g2,infoTextX+280,770,120,65,10,buttonStroke,sellColor);
        drawStringAtCenter(g2,"Hypothek", infoTextX+340, 770+25, smallTextFont);
        drawStringAtCenter(g2,"Aufnehmen", infoTextX+340, 770+55, smallTextFont);
    }

    private final void drawStringAtCenter(Graphics2D g2,String text, int xCenter, int y, Font font) {
        g2.setColor(Color.WHITE);
        g2.setFont(font);
        g2.drawString(text, xCenter-(utils.stringWidth(g2,text)/2), y);
    }


    private final void drawButton(Graphics2D g2, int x, int y, int width, int height, int arc, Stroke stroke, Color color) {
        if (stroke != null) {
            g2.setStroke(stroke);
            g2.setColor(Color.black);
            g2.drawRoundRect(x,y,width,height,arc,arc);
        }
        g2.setColor(color);
        g2.fillRoundRect(x,y,width,height,arc,arc);
    }

    private final Font textFont;
    private final int infoTextX = 175;
    private final int infoTextY = 225;
    private final int infoTextCostX = 575;
    private final int infoTextCostY = 225;
    private void drawHousePrices(@NotNull Graphics2D g2, Street street) {
        g2.setFont(textFont);
        g2.setColor(Color.black);
        g2.drawString("1 Haus kostet", infoTextX, (int) (infoTextY + 400 * scaleFactor));

        g2.drawString(String.format("%d €", street.getHousePrice()),
                infoTextCostX - utils.stringWidth(g2, String.format("%d €", street.getHousePrice())),
                (int) (infoTextY + 400 * scaleFactor));

        g2.drawString("1 Hotel - 4 Häuser u.", infoTextX, (int) (infoTextY + 430 * scaleFactor));
        g2.drawString(String.format("%d €", street.getHousePrice()),
                infoTextCostX - utils.stringWidth(g2, String.format("%d €", street.getHousePrice())),
                (int) (infoTextY + 430 * scaleFactor));
    }

    private void drawRentPrices(@NotNull Graphics2D g2, Street street) {
        String[] information = {"Miete Grundstück allein", "    mit 1 Haus", "         2 Häusern",
                "         3 Häusern", "         4 Häusern", "         1 Hotel","Aktuelle Miete", "Monopol", "Gebaute Häuser", "Hypotheken Wert"};



        for (int i = 1; i < information.length + 1; i++) {

            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (50 + (i * 30)) * scaleFactor));
            String text;
            if (i < 7) {
                text = String.format("%d €", street.getPriceList().get(i - 1));

            } else if (i== 7){
                text = String.format("%d €", street.getRent());
            } else if (i == 8) {
                if (street.monopolyCheck()) {
                    text = "Ja";
                } else {
                    text = "Nein";
                }
            } else if (i == 9) {
                text = String.format("%d", street.getHouses());
            } else {
                text = String.format("%d €", street.getBuyPrice()/2);
            }
            g2.drawString(text,
                    infoTextCostX - utils.stringWidth(g2, text),
                    (int) (infoTextCostY + (50 + (i * 30)) * scaleFactor));
        }
    }
    private void drawStreetInformation(@NotNull Graphics2D g2, Street street) {
        String[] information = {"Monopol", "Gebaute Häuser", "Hypotheken Wert",
                "         3 Häusern", "         4 Häusern", "         1 Hotel","         Aktuelle Miete"};

        for (int i = 1; i < information.length + 1; i++) {

            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (50 + (i * 30)) * scaleFactor));
            String text;
            if (i < information.length) {
                text = String.format("%d €", street.getPriceList().get(i - 1));

            } else {
                text = String.format("%d €", street.getRent());
            }
            g2.drawString(text,
                    infoTextCostX - utils.stringWidth(g2, text),
                    (int) (infoTextCostY + (50 + (i * 30)) * scaleFactor));
        }
    }

    private int detailItem;
    private boolean showDetails;
    private void showDetails(int index) {
        this.detailItem = index;
        this.showDetails = true;
    }
}
