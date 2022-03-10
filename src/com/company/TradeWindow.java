package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class TradeWindow {
    private final Board bp;
    private final Player player;
    private final UI parentUI;
    private final Property prop;
    private final ArrayList<Player> playerList;

    private final Image acceptImage;
    private final Image closeImage;
    private final double scaleFactor;

    private final int closeButtonX;
    private final int closeButtonY;
    private final int acceptButtonX;
    private final int acceptButtonY;
    private final int buttonWidth;
    private final int buttonHeight;
    private final Inventory parentInv;
    public TradeWindow(Player player, UI parentUI, Board bp, Property prop, Inventory parentInv) {
        this.bp = bp;
        this.player = player;
        this.parentUI = parentUI;
        this.prop = prop;
        this.playerList = bp.getPlayerList();
        this.parentInv = parentInv;

        scaleFactor = parentUI.getScaleFactor();
        outerStroke  = new BasicStroke((float) (10*scaleFactor));
        innerStroke  = new BasicStroke((float) (5*scaleFactor));

        menuX = (int) (240*scaleFactor);
        menuY = (int) (240*scaleFactor);
        menuWidth = (int) (600*scaleFactor);
        menuHeight = (int) (600*scaleFactor);
        menuArc = (int) (50*scaleFactor);

        priceX = menuX+((menuWidth/2)-(250/2));
        priceY = 750;
        priceWidth=250;
        priceHeight=60;

        closeButtonX = menuX+10;
        closeButtonY=menuY+menuHeight-70;
        acceptButtonY=menuY+menuHeight-70;
        buttonWidth=60;
        buttonHeight=60;
        acceptButtonX = menuX+menuWidth-buttonWidth-10;

        acceptImage = utils.loadImage("images/tick.png",buttonWidth,buttonHeight);
        closeImage = utils.loadImage("images/close.png",buttonWidth,buttonHeight);


        nameBox = new JTextArea();
        nameBox.setEditable(true);
        nameBox.setBounds(priceX, priceY, priceWidth, priceHeight);
        nameBox.setOpaque(false);
        emptyTextColor = new Color(200, 200, 200, 180);
        nameFont = new Font("Roboto", Font.PLAIN, (int) (20 * scaleFactor));

        closeButton = parentUI.createButton(buttonWidth,buttonHeight,closeButtonX,closeButtonY,closeListener);
        acceptButton = parentUI.createButton(buttonWidth,buttonHeight,acceptButtonX,acceptButtonY,acceptListener);
    }
    private final ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            disable();
            parentInv.disableTradeWindow();
        }
    };
    private final ActionListener acceptListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (targetPlayer.removeMoney(Integer.parseInt(nameBox.getText()))) {
                for (Property prop: selectedList) {
                    player.addProperty(prop, true);
                }
                disable();
                parentInv.disableTradeWindow();
            }
        }

    };
    private void disable() {
        parentUI.disableButtons(new ArrayList<>(Arrays.asList(closeButton, acceptButton)));
        parentUI.disableButtons(btnList);
    }
    private JButton closeButton;
    private JButton acceptButton;
    private final JTextArea nameBox;
    private final Stroke innerStroke;
    private final Color innerColor = new Color(88, 88, 88,220);
    private final Stroke outerStroke;
    public void draw(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(outerStroke);
        g2.drawRoundRect(menuX,menuY,menuWidth,menuHeight,menuArc,menuArc);
        g2.setColor(innerColor);
        g2.fillRoundRect(menuX,menuY,menuWidth,menuHeight,menuArc,menuArc);
        if(!chosen) {
            drawPlayers(g2);
        } else {
            drawSum(g2);
            drawSelectableProperties(g2);
        }
    }
    private final int menuX;
    private final int menuY;
    private final int menuWidth;
    private final int menuHeight;
    private final int menuArc;
    private Font nameFont;

    private void drawPlayers(Graphics2D g2) {
        int i = 0;
        for (Player p: playerList) {

            if (p != player) {
                int x = 300 + (280*(i%2));
                int y = 300 + (280*(i/2));
                g2.drawImage(p.getIcon(), x,y,200,200,null);
                g2.setColor(Color.white);
                g2.setStroke(innerStroke);
                g2.drawRoundRect(x,y,200,200,20,20);
                i++;
                int finalI = i;
                ActionListener playerListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setDrawSum(finalI, p);
                        chosen = true;
                    }
                };
                btnList.add(parentUI.createButton(200,200,x,y,playerListener));
            }
        }
    }
    private ArrayList<JButton> btnList = new ArrayList<>();
    private boolean chosen;
    private Player targetPlayer;
    private void setDrawSum(int index, Player p) {
        this.drawIndex = index;
        this.targetPlayer = p;
        parentUI.disableButtons(btnList);
        bp.add(nameBox);
        targetPlayerProperties = targetPlayer.getProperties();
        int height = 50;
        int width = 180;

        for(int i =0; i<targetPlayerProperties.size(); i++) {
            int x = menuX + 10 + (200*(i%3));
            int y = menuY + 20 + (60*(i/3));
            propButtonList.add(parentUI.createButton(width,height,x,y, propListener));
        }

    }
    private int drawIndex;
    private int page;
    private final int priceX;
    private final int priceY;
    private final int priceWidth;
    private final int priceHeight;
    private final Color emptyTextColor;
    private void drawSum(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(innerStroke);
        g2.drawRoundRect(priceX,priceY,priceWidth,priceHeight,25,25);

        g2.drawImage(acceptImage, acceptButtonX, acceptButtonY ,buttonWidth,buttonHeight,null);
        g2.drawImage(closeImage, closeButtonX, closeButtonY ,buttonWidth,buttonHeight,null);

        if (nameBox.getText().isEmpty()) {
            g2.setColor(emptyTextColor);
            g2.drawString("Price", priceX+20, priceY+50);
        } else {
            //Sanitizing String
            if (!nameBox.getText().matches("[0-9]+")) {
                nameBox.setText(nameBox.getText().substring(0, nameBox.getText().length() - 1));
            }
            if (Integer.parseInt(nameBox.getText()) > targetPlayer.getMoney()) {
                //Removing last character
                nameBox.setText(Integer.toString(targetPlayer.getMoney()));
            }
            g2.setColor(Color.white);
            g2.drawString(nameBox.getText(), priceX+20, priceY+50);
        }
    }
    private ArrayList<Property> targetPlayerProperties;
    private void drawSelectableProperties(Graphics2D g2) {

        int height = 50;
        int width = 180;
        for(int i = 0; i < targetPlayerProperties.size(); i++) {
            int x = menuX + 10 + (200*(i%3));
            int y = menuY + 20 + (60*(i/3));
            Property prop = targetPlayerProperties.get(i);

            g2.setStroke(innerStroke);
            if (selectedList.contains(prop)){
                g2.setColor(selectedColor);
            } else {
                g2.setColor(Color.black);
            }
            g2.drawRoundRect(x,y,width,height,25,25);
            if (targetPlayerProperties.get(i) instanceof Street street) {
                g2.setColor(street.getColor());
            } else {
                g2.setColor(Color.white);
            }
            g2.fillRoundRect(x,y,width,height,25,25);
            g2.setColor(Color.black);
            g2.setFont(utils.getFittingFont(g2, prop.getName(),nameFont,width));
            g2.drawString(prop.getName(), x+(width/2)-(utils.stringWidth(g2, prop.getName())/2) ,y+30);

        }
    }
    private ArrayList<JButton> propButtonList = new ArrayList<>();
    private final Color selectedColor = new Color(21, 231, 0);
    private final ActionListener propListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] coordinateString = e.getSource().toString().substring(21, 28).split(",", 2);

            int x = Integer.parseInt(coordinateString[0]);
            int y = Integer.parseInt(coordinateString[1]);
            for (int i=0; i<targetPlayer.getProperties().size();i++) {
                if (x == menuX + 10 + (200*(i%3))) {
                    x = i%3;
                }
                if (y == menuY + 20 + (60*(i/3))) {
                    y = (i/3)*3;
                }
            }
            int index = y+x;
            if (index < targetPlayer.getProperties().size()) {
                if (selectedList.contains(targetPlayerProperties.get(index))) {
                    selectedList.remove(targetPlayerProperties.get(index));
                } else {
                    selectedList.add(targetPlayerProperties.get(index));
                }
            }
        }
    };
    private ArrayList<Property> selectedList = new ArrayList<>();

}
