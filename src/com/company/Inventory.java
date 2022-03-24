package com.company;

import com.company.exceptions.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

public class Inventory {

    private final Color buyColor = new Color(12, 156, 13);
    private final Color sellColor = new Color(217, 53, 53);
    private final Color warningColor = new Color(209, 82, 15);
    private final Color menuColor = new Color(220, 220, 220);

    private final Player player;
    private final Board bp;
    private final UI parentUI;
    private final double scaleFactor;
    private int page = 0;
    private int detailItem;
    private boolean showDetails;
    private int pages;

    private final ArrayList<Player> players = new ArrayList<>();
    private final Stroke outerCardStroke = new BasicStroke(8);
    private final Stroke buttonStroke = new BasicStroke(4);
    private final Stroke outerStroke = new BasicStroke(10);
    private final Stroke divideStroke = new BasicStroke(1);

    private final Font nameFont;
    private final Font smallNameFont;
    private final Font smallTextFont;
    private final Font textFont;

    private final int firstColumnButtonX;
    private final int secondColumnButtonX;
    private final int thirdColumnButtonX;
    private final int firstRowButtonY;
    private final int secondRowButtonY;
    private final int buttonWidth;
    private final int buttonHeight;

    private final int infoTextX;
    private final int infoTextY;
    private final int infoTextCostX;
    private final int infoTextCostY;
    //private final JButton nextPageButton;
    //private final JButton lastPageButton;
    //private final JButton closePageButton;
    //private final JButton acceptPageButton;
    //private final JButton closeInventoryButton;

    private final ArrayList<JButton> streetButtons = new ArrayList<>();
    private final ArrayList<JButton> propertyButtons = new ArrayList<>();
    private ArrayList<Property> propertyList = new ArrayList<>();

    private JButton backButton;
    private JButton buyHouseButton;
    private JButton sellHouseButton;
    private JButton unMortgageButton;
    private JButton mortgageButton;

    private final ActionListener closeInventoryListener = e -> {
        end();
    };
    private final ActionListener buyHouseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ((Street) propertyList.get(detailItem)).buyHouse();
            } catch (maxHousesPerPropertyReachedException ex) {
                //TODO: ADD WARNING
                ex.printStackTrace();
            }
        }
    };
    private final ActionListener sellHouseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ((Street) propertyList.get(detailItem)).sellHouse(1);
            } catch (negativeHousesException ex) {
                //TODO: ADD WARNING
                ex.printStackTrace();
            }
        }
    };
    private final ActionListener backListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disableDetails();
        }
    };
    private final ActionListener mortgageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                propertyList.get(detailItem).mortgage();
            } catch (alreadyMortgagedException ex) {
                ex.printStackTrace();
            } catch (cannotMortgageHousedPropertyException ex) {
                ex.printStackTrace();
            }
            disableDetails();
        }
    };
    private final ActionListener unMortgageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                propertyList.get(detailItem).unmortgage();
            } catch (notMortgagedException ex) {
                ex.printStackTrace();
            }
            disableDetails();
        }
    };

    private final int entriesPerPage = 21;

    public Inventory(Player p, Board bp, UI parentUI) {
        this.player = p;
        this.bp = bp;
        this.parentUI = parentUI;
        pages = player.getProperties().size()/entriesPerPage+1;
        //for (int i = 0; i< player.getProperties().size(); i++) {
        //parentUI.createButton(100,50)
        //}
        scaleFactor = parentUI.getScaleFactor();
        nameFont = new Font("Roboto", Font.PLAIN, (int) (35 * scaleFactor));
        smallNameFont = new Font("Roboto", Font.PLAIN, (int) (30 * scaleFactor));
        textFont = new Font("Roboto", Font.PLAIN, (int) (25 * scaleFactor));
        smallTextFont = new Font("Roboto", Font.PLAIN, (int) (20 * scaleFactor));

        buttonHeight = (int) (65 * scaleFactor);
        buttonWidth = (int) (120 * scaleFactor);
        firstRowButtonY = (int) (690 * scaleFactor);
        secondRowButtonY = (int) (firstRowButtonY + (80 * scaleFactor));
        firstColumnButtonX = (int) (175 * scaleFactor);
        secondColumnButtonX = (int) (firstColumnButtonX + (140 * scaleFactor));
        thirdColumnButtonX = (int) (secondColumnButtonX + (140 * scaleFactor));

        menuRectX = (int) (90 * scaleFactor);
        menuRectY= (int) (90*scaleFactor);
        menuRectHeight = (int) (900*scaleFactor);
        menuRectWidth = (int) (900*scaleFactor);

        menuButtonY = (int) (parentUI.getHeight()-menuRectY-(70*scaleFactor));
        menuButtonHeight = (int) (60*scaleFactor);
        menuButtonWidth = (int) (60*scaleFactor);

        closeButtonX = menuRectX+(menuRectWidth/2)-(menuButtonWidth/2);
        nextButtonX = (int) (parentUI.getWidth()-menuRectX-(menuButtonWidth/2));
        lastButtonX= (int) (menuRectX+menuButtonWidth/2);

        closeButtonImage = utils.loadImage("images/close.png", 60,60);
        nextPageImage = utils.loadImage("images/next.png", 60,60);
        lastPageImage = utils.loadImage("images/back.png", 60,60);

        nextPageButton = parentUI.createButton(menuButtonWidth, menuButtonHeight, nextButtonX, menuButtonY, nextPageListener);
        lastPageButton = parentUI.createButton(menuButtonWidth, menuButtonHeight, lastButtonX, menuButtonY, lastPageListener);
        closeInventoryButton = parentUI.createButton(menuButtonWidth, menuButtonHeight, closeButtonX, menuButtonY, closeInventoryListener);

        sortedPropertyList = player.getPropertiesSorted();
        infoTextCostY = (int) (225*scaleFactor);
        infoTextCostX = (int) (575*scaleFactor);
        infoTextX = (int) (175*scaleFactor);
        infoTextY = (int) (225*scaleFactor);

        detailRectX = (int) (150*scaleFactor);
        detailRectY= (int) (150*scaleFactor);
        detailRectHeight= (int) (700*scaleFactor);
        detailRectWidth= (int) (450*scaleFactor);
        detailRectArc= (int) (50*scaleFactor);
        menuRectArc = (int) (50*scaleFactor);

        streetWidth = (int) (250*scaleFactor);
        streetHeight = (int) (70*scaleFactor);
        titleArc = (int) (15*scaleFactor);

        detailX= (int) (detailRectX + (225-125)*scaleFactor);
        detailY = (int) (175*scaleFactor);
        detailWidth= (int) (250*scaleFactor);
        detailHeight = (int) (70*scaleFactor);
        dividerX = (int) (175*scaleFactor);
    }

    private final ActionListener nextPageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    private final ActionListener lastPageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    private final int closeButtonX;
    private final int menuButtonWidth;
    private final int menuButtonHeight;
    private final Image closeButtonImage;
    private final int menuRectX;
    private final int menuRectY;
    private final int menuRectWidth;
    private final int menuRectHeight;
    private final int nextButtonX;
    private final int lastButtonX;
    private final int menuButtonY;
    private final Image nextPageImage;
    private final Image lastPageImage;

    private JButton nextPageButton;
    private JButton lastPageButton;
    private JButton closeInventoryButton;

    private void enableMenuButtons() {
        parentUI.getBp().add(nextPageButton);
        parentUI.getBp().add(lastPageButton);
        parentUI.getBp().add(closeInventoryButton);
    }

    private void disableMenuButtons() {
        parentUI.disableButtons(new ArrayList<>(Arrays.asList(nextPageButton,lastPageButton,closeInventoryButton)));
    }

    public void drawMenuButtons(Graphics2D g2) {
        g2.drawImage(closeButtonImage, closeButtonX, menuButtonY, null);
        if (pages > page+1) {
            g2.drawImage(nextPageImage, nextButtonX, menuButtonY, null);
        } else if (pages < page+1) {
            System.out.println(pages);
            System.out.println(page);
            g2.drawImage(lastPageImage, lastButtonX, menuButtonY, null);
        }
    }
    private final int detailRectX;
    private final int detailRectY;
    private final int detailRectWidth;
    private final int detailRectHeight;
    private final int detailRectArc;
    private final int menuRectArc;
    public void draw(Graphics2D g2) {


        if (tradeWindow == null) {
            if (showDetails) {
                g2.setStroke(outerStroke);
                g2.setColor(Color.black);
                g2.drawRoundRect(detailRectX, detailRectY, detailRectWidth, detailRectHeight, detailRectArc, detailRectArc);
                g2.setColor(menuColor);
                g2.fillRoundRect(detailRectX, detailRectY, detailRectWidth, detailRectHeight, detailRectX, detailRectArc);
                drawDetails(g2, detailItem);
            } else {
                g2.setStroke(outerStroke);
                g2.setColor(Color.black);
                g2.drawRoundRect(menuRectX, menuRectY, menuRectWidth, menuRectHeight, menuRectArc, menuRectArc);
                g2.setColor(menuColor);
                g2.fillRoundRect(menuRectX, menuRectY, menuRectWidth, menuRectHeight, menuRectArc, menuRectArc);
                drawProperties(g2);
                drawMenuButtons(g2);
            }
        }
        else {
            tradeWindow.draw(g2);
        }
    }

    private void end() {
        parentUI.closeInventory();
    }

    public ArrayList<JButton> getButtons() {
        ArrayList<JButton> tmpArray = new ArrayList<>(Arrays.asList(nextPageButton, lastPageButton,closeInventoryButton));
        tmpArray.addAll(streetButtons);
        return tmpArray;
    }
    private JButton tradeButton;
    private Hashtable<Integer, ArrayList<Property>> sortedPropertyList;
    private final int streetWidth;
    private final int streetHeight;
    private void drawProperties(Graphics2D g2) {


        Enumeration<Integer> e = sortedPropertyList.keys();

        g2.setColor(Color.black);
        g2.setFont(nameFont);
        g2.setStroke(outerCardStroke);

        int i = 0;
        while (e.hasMoreElements()) {

            int key = e.nextElement();
            if (i < entriesPerPage) {
                ArrayList<Property> sameIndexList = sortedPropertyList.get(key);
                for (Property property : sameIndexList) {

                    int x = (int) ((130 + (285 * (i % 3)))*scaleFactor);
                    int y = (int) ((140 + ((i / 3) * 100))*scaleFactor);


                    drawTitle(g2, property, x, y, streetWidth, streetHeight);

                    int finalI = i+(entriesPerPage*page);
                    ActionListener detailListener = e1 -> showDetails(finalI);

                    streetButtons.add(parentUI.createButton(streetWidth, streetHeight, x, y, detailListener));
                    propertyList.add(property);
                    i++;
                }
            }
        }
    }
    private final Color mortgageColor = new Color(108, 108, 108);
    private final int titleArc;
    private void drawTitle(Graphics2D g2, Property prop, int x, int y, int width, int height) {

        g2.setFont(nameFont);
        g2.setStroke(outerCardStroke);
        g2.setColor(Color.black);

        g2.drawRoundRect(x, y, width, height, titleArc, titleArc);
        if (prop.isMortgaged()) {
            g2.setColor(mortgageColor);
        } else {
            if (prop instanceof Street) {
                g2.setColor(((Street) prop).getColor());
            } else {
                g2.setColor(Color.white);
            }
        }
        g2.fillRoundRect(x, y, width, height, titleArc, titleArc);
        g2.setColor(Color.black);

        if (utils.stringWidth(g2, prop.getName()) > (width - 20*scaleFactor)) {
            g2.setFont(utils.getFittingFont(g2, prop.getName(), nameFont, (int) (width - 20*scaleFactor)));
        }
        g2.drawString(prop.getName(), (x + width / 2) - (utils.stringWidth(g2, prop.getName()) / 2), (int) (y + (height / 1.5)));
    }
    private final int detailX;
    private final int detailY;
    private final int detailWidth;
    private final int detailHeight;
    private final int dividerX;
    private void drawDetails(Graphics2D g2, int index) {
        int x = detailRectX + 225 - 125;
        int y = 175;
        int width = 250;
        int height = 70;


        parentUI.disableButtons(streetButtons);

        drawTitle(g2, propertyList.get(index), detailX, detailY, detailWidth, detailHeight);

        if (propertyList.get(index) instanceof Street) {
            drawHousePrices(g2, (Street) propertyList.get(index));
            drawStreetInformation(g2, (Street) propertyList.get(index));
            drawStreetButtons(g2, (Street) propertyList.get(index));
            drawDivider(g2, dividerX, (int) (670*scaleFactor));
            drawDivider(g2, dividerX, (int) (590*scaleFactor));
        } else if (propertyList.get(index) instanceof TrainStation) {
            //drawTrainButtons(g2, (TrainStation) propertyList.get(index));
            drawButtons(g2, propertyList.get(index));
            drawDivider(g2, dividerX, (int) (500*scaleFactor));
            drawTrainInformation(g2, (TrainStation) propertyList.get(index));
        } else if (propertyList.get(index) instanceof UtilityCompany) {
            drawButtons(g2, propertyList.get(index));
            drawDivider(g2, dividerX, (int) (390*scaleFactor));
            drawDivider(g2, dividerX, (int) (530*scaleFactor));
            drawUtilityInformation(g2, (UtilityCompany) propertyList.get(index));
        }
    }

    private void drawTrainInformation(Graphics2D g2, TrainStation trainStation) {

        String[] information = {"Miete", "    mit 2 Bahnhöfen", "         3 Bahnhöfen",
                "         4 Bahnhöfen", "Aktuelle Miete", "Bahnhöfe in Besitz", "Hypotheken Wert"};

        g2.setFont(textFont);
        for (int i = 1; i < information.length + 1; i++) {

            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (50 + (i * 30)) * scaleFactor));

            String text;

            int rent = trainStation.getRent();

            if (i < 5) {
                text = String.format("%d€", (int) Math.pow(2, i - 1) * rent);
            } else if (i == 5) {
                text = String.format("%d €", trainStation.getCurrentRent());
            } else if (i == 6) {
                text = String.valueOf(trainStation.getOwnedTrainStations().size());
            } else {
                text = String.format("%d €", trainStation.getBuyPrice()/2);
            }

            g2.drawString(text,
                    infoTextCostX - utils.stringWidth(g2, text),
                    (int) (infoTextCostY + (50 + (i * 30)) * scaleFactor));
        }

    }

    private void drawUtilityInformation(Graphics2D g2, UtilityCompany utilityCompany) {

        g2.setFont(textFont);
        g2.setColor(Color.black);

        g2.drawString(String.format("Stationen in Besitz: %d",
                        utilityCompany.getOwnedUtilityCompanies().size()),
                infoTextX, (int) (infoTextY + 350 * scaleFactor));

        String firstDescription = "Wenn man Besitzer eines Werkes ist so ist die Miete 80mal so hoch, wie Augen auf den Würfel sind";
        String secondDescription = "Wenn man Besitzer beider Werke, Elektrizitäts- und Wasser-Werk, ist, so ist die Miete 200mal so hoch wie Augen auf den Würfeln sind";

        int j = drawLongText(g2, firstDescription, 1, 1, false);
        drawLongText(g2, secondDescription, j, 2, false);

    }
    protected int drawLongText(Graphics2D g2,String text, int startingIndex,int paragraph, boolean centered) {
        g2.setColor(Color.black);
        StringBuilder tmpString = new StringBuilder();
        for (String s: text.split(" ")) {
            if (s.equals("\n")) {
                drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
                startingIndex++;
                tmpString = new StringBuilder(s);
            }
            else if (utils.stringWidth(g2, tmpString + " " + s) > (detailRectWidth-40*scaleFactor)) {
                drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
                startingIndex++;
                tmpString = new StringBuilder(s);
            } else {
                if (!tmpString.isEmpty()) {

                    tmpString.append(" ").append(s);
                } else {
                    tmpString.append(s);
                }
            }
        }
        drawLine(tmpString.toString(), g2, centered,startingIndex,paragraph);
        startingIndex++;
        return startingIndex;
    }
    private void drawLine(String text, Graphics2D g2, boolean centered, int index, int paragraph) {

        int y = (int) (infoTextY + (75 + ((index + (paragraph / 2)) * 25)) * scaleFactor);

        if (centered) {
            g2.drawString(text, (detailRectWidth/2)-(utils.stringWidth(g2, text)/2)+150, y);
        } else {
            g2.drawString(text, infoTextX, y);
        }
    }

    protected void drawDivider(@NotNull Graphics2D g2, int x, int y) {
        g2.setColor(Color.black);
        g2.setStroke(divideStroke);
        g2.drawRect(x, y, (int) (400 * scaleFactor), 1);
    }

    private void drawStreetButtons(Graphics2D g2, Street street) {
        if (buyHouseButton == null) {
            buyHouseButton = parentUI.createButton(buttonWidth, buttonHeight, infoTextX, firstRowButtonY, buyHouseListener);
            sellHouseButton = parentUI.createButton(buttonWidth, buttonHeight, firstColumnButtonX, secondRowButtonY, sellHouseListener);
        }
        drawButton(g2, firstColumnButtonX, firstRowButtonY, buttonWidth, buttonHeight, 10, buttonStroke, buyColor);
        drawStringAtCenter(g2, "Haus Kaufen", firstColumnButtonX + (buttonWidth / 2), firstRowButtonY + 25, smallTextFont);
        drawStringAtCenter(g2, String.format("%d €", street.getHousePrice()), firstColumnButtonX + (buttonWidth / 2), firstRowButtonY + 55, smallTextFont);



        drawButton(g2, firstColumnButtonX, secondRowButtonY, buttonWidth, buttonHeight, 10, buttonStroke, sellColor);
        drawStringAtCenter(g2, "Verkaufen", firstColumnButtonX + (buttonWidth / 2), secondRowButtonY + 40, smallTextFont);
        drawStringAtCenter(g2, String.format("%d €", street.getHousePrice() / 2), secondColumnButtonX + (buttonWidth / 2), secondRowButtonY + 55, smallTextFont);

        drawButton(g2, secondColumnButtonX, firstRowButtonY, buttonWidth, buttonHeight, 10, buttonStroke, warningColor);
        drawStringAtCenter(g2, "Handeln", secondColumnButtonX + (buttonWidth / 2), firstRowButtonY + 40, textFont);

        drawStandardButtons(g2, secondColumnButtonX, thirdColumnButtonX, firstRowButtonY, secondRowButtonY, buttonWidth,buttonHeight);


    }
    private void drawButtons(Graphics2D g2 ,Property prop) {
        int width = (int) (buttonWidth*1.5);
        int secondColumnX = secondColumnButtonX+(buttonWidth/2)+20;
        drawStandardButtons(g2, firstColumnButtonX, secondColumnX, firstRowButtonY, secondRowButtonY, width,buttonHeight);


    }
    public void disableTradeWindow() {
        tradeWindow = null;
        tradeButton = null;
        buyHouseButton = null;
    }

    private final Inventory openInventory = this;
    public void drawStandardButtons(Graphics2D g2, int firstX,int secondX, int firstY, int secondY, int width, int height) {
        if (tradeButton == null) {
            System.out.println(detailItem);
            final ActionListener tradeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tradeWindow = new TradeWindow(player, parentUI, bp, propertyList.get(detailItem), openInventory);
                    parentUI.disableButtons(getDetailButtons());
                }
            };
            tradeButton = parentUI.createButton(width,height,firstX, firstY, tradeListener);
            backButton = parentUI.createButton(width, height, firstX, secondY, backListener);
            unMortgageButton = parentUI.createButton(width, height, secondX, secondY, unMortgageListener);
            mortgageButton = parentUI.createButton(width, height, secondX, firstY, mortgageListener);
        }
        drawButton(g2, firstX, firstY, width, height, 10, buttonStroke, warningColor);
        drawStringAtCenter(g2, "Handeln", firstX + (width / 2), firstY + 40, textFont);


        drawButton(g2, firstX, secondY, width, height, 10, buttonStroke, sellColor);
        drawStringAtCenter(g2, "Zurück", firstX + (width / 2), secondY + 40, textFont);

        drawButton(g2, secondX, secondY, width, height, 10, buttonStroke, buyColor);
        drawStringAtCenter(g2, "Hypothek", secondX + (width / 2), secondY + 25, smallTextFont);
        drawStringAtCenter(g2, "Auflösen", secondX + (width / 2), secondY + 55, smallTextFont);

        drawButton(g2, secondX, firstY, width, height, 10, buttonStroke, sellColor);
        drawStringAtCenter(g2, "Hypothek", secondX + (width / 2), firstY + 25, smallTextFont);
        drawStringAtCenter(g2, "Aufnehmen", secondX + (width / 2), firstY + 55, smallTextFont);



    }


    private void drawStringAtCenter(Graphics2D g2, String text, int xCenter, int y, Font font) {
        g2.setColor(Color.WHITE);
        g2.setFont(font);
        g2.drawString(text, xCenter - (utils.stringWidth(g2, text) / 2), y);
    }

    private void drawButton(Graphics2D g2, int x, int y, int width, int height, int arc, Stroke stroke, Color color) {
        if (stroke != null) {
            g2.setStroke(stroke);
            g2.setColor(Color.black);
            g2.drawRoundRect(x, y, width, height, arc, arc);
        }
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, arc, arc);
    }

    private void drawHousePrices(@NotNull Graphics2D g2, @NotNull Street street) {
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

    private void drawStreetInformation(@NotNull Graphics2D g2, Street street) {
        String[] information = {"Miete Grundstück allein", "    mit 1 Haus", "         2 Häusern",
                "         3 Häusern", "         4 Häusern", "         1 Hotel", "Aktuelle Miete", "Monopol", "Gebaute Häuser", "Hypotheken Wert"};


        for (int i = 1; i < information.length + 1; i++) {

            g2.drawString(information[i - 1], infoTextX, (int) (infoTextY + (50 + (i * 30)) * scaleFactor));

            String text;

            if (i < 7) {
                text = String.format("%d €", street.getPriceList().get(i - 1));
            } else if (i == 7) {
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
                text = String.format("%d €", street.getBuyPrice() / 2);
            }

            g2.drawString(text,
                    infoTextCostX - utils.stringWidth(g2, text),
                    (int) (infoTextCostY + (50 + (i * 30)) * scaleFactor));
        }
    }

    private void showDetails(int index) {
        this.detailItem = index;
        this.showDetails = true;
        disableMenuButtons();
    }

    private void disableDetails() {
        this.showDetails = false;
        parentUI.disableButtons(getDetailButtons());
        enableMenuButtons();
        this.detailItem = 0;
        this.sortedPropertyList = player.getPropertiesSorted();
    }


    private TradeWindow tradeWindow;
    private ArrayList<JButton> getDetailButtons() {
        //TODO: ADD TRADE BUTTON
        if (propertyList.get(detailItem) instanceof Street) {
            return new ArrayList<>(Arrays.asList(buyHouseButton, unMortgageButton, mortgageButton, backButton, sellHouseButton, tradeButton));
        } else {
            return new ArrayList<>(Arrays.asList(unMortgageButton,mortgageButton,backButton, tradeButton));
        }
    }
}
