package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class UI {

    private final Board bp;

    private final JButton rollButton;
    private final JButton nextButton;

    private final setupUI setupui;
    private boolean setup = true;
    private boolean nextTurn = false;

    private final Image nextTurnImage;
    private final Image diceImage;
    private final Image nextTurnImageGrey;
    private final ArrayList<Image> imgNumbers;
    private final ArrayList<Image> imgNumbersGolden;

    private ArrayList<Player> playerList;
    private Player currentPlayer;

    private final int screenWidth;
    private final int screenHeight;
    private final double scaleFactor;

    private boolean nextTurnAvailable = false;
    private int[] finalRoll;
    private boolean displayDice = false;

    private final ActionListener diceListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!displayDice){
                finalRoll = currentPlayer.roll();
                displayDice = true;
            }
        }
    };
    private final ActionListener nextListener = e -> {
        if (nextTurnAvailable) { nextTurn = true; }
    };

    public UI(Board bp) {
        this.bp = bp;

        screenHeight = bp.getScreenHeight();
        screenWidth = bp.getScreenWidth();
        scaleFactor = screenHeight/1080.0;

        setupui = new setupUI(this.bp,this);

        //Loading Images
        diceImage = utils.loadImage("images/dice.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        nextTurnImage = utils.loadImage("images/nextTurnArrow.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        nextTurnImageGrey = utils.loadImage("images/nextTurnArrowGrey.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        imgNumbers = loadNumberImages((int) (200*scaleFactor),(int) (200*scaleFactor), false);
        imgNumbersGolden = loadNumberImages((int) (200*scaleFactor), (int) (200*scaleFactor), true);

        //Creating Buttons
        rollButton = createButton((int) (100*scaleFactor),
                (int) (100*scaleFactor),
                (int) ((screenWidth/2)-60*scaleFactor),
                (int) (screenHeight/1.35),
                diceListener);
        nextButton = createButton((int) (100*scaleFactor),
                (int) (100*scaleFactor),
                (int) ((screenWidth/2)-180*scaleFactor),
                (int) (screenHeight/1.35), nextListener);
        //TODO: Add different buttons
    }

    private ArrayList<Image> loadNumberImages(int width, int height, boolean golden) {
        ArrayList<Image> loadList= new ArrayList<>();
        for(int i=0;i<6;i++) {
            String path;
            if (golden){
                path = String.format("images/numbers/golden/%d.png",i+1);
            }
            else {
                path = String.format("images/numbers/%d.png",i+1);
            }
            loadList.add(utils.loadImage(path, width,height));
        }
        return loadList;
    }

    protected JButton createButton(int width, int height, int x, int y, ActionListener btnListener) {
        JButton newButton = new JButton();
        newButton.setLocation(x, y);
        newButton.setSize(width, height);

        //Making button Invisible
        newButton.setVisible(true);
        newButton.setOpaque(false);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);

        newButton.addActionListener(btnListener);

        //Adding button to board
        bp.add(newButton);
        return newButton;
    }

    private StreetInfo streetInfo;
    public void drawStreetInfo(StreetInfo info){
        streetInfo = info;
    }

    //Generating first random numbers to display when rolling
    Random rand = new Random();
    private int firstDice = rand.nextInt(1, 7);
    private int secondDice = rand.nextInt(1, 7);
    private int diceCounter;

    private int noDuplicateDice(int firstInt, int secondInt) {
        if (firstInt != secondInt) {
            return firstInt;
        } else if (firstInt+1 < 7) {
            return firstInt + 1;
        } else {return firstInt - 1;}
    }

    private void drawRandomDice(Graphics2D g2) {
        if (diceCounter % 5 == 0) {
            firstDice = rand.nextInt(1, 7);
            secondDice = rand.nextInt(1, 7);

            //Making sure new random dice is different to last one
            firstDice = noDuplicateDice(rand.nextInt(1,7), firstDice);
            secondDice = noDuplicateDice(rand.nextInt(1,7), secondDice);
        }
        //Draw both dice
        g2.drawImage(imgNumbers.get(firstDice-1), screenWidth/2-175, 200, null);
        g2.drawImage(imgNumbers.get(secondDice-1), screenWidth/2-25, 200, null);
        diceCounter += 1;
    }

    private void drawFinalDice(Graphics2D g2) {
        if (finalRoll[0] == finalRoll[1]) {
            g2.drawImage(imgNumbersGolden.get(finalRoll[0]-1), screenWidth/2-175, 150, null);
            g2.drawImage(imgNumbersGolden.get(finalRoll[1]-1), screenWidth/2-25, 150, null);
        } else {
            g2.drawImage(imgNumbers.get(finalRoll[0]-1), screenWidth/2-175, 150, null);
            g2.drawImage(imgNumbers.get(finalRoll[1]-1), screenWidth/2-25, 150, null);
        }
    }



    public void draw(Graphics2D g2) {
        if (setup) { setupui.draw(g2); }
        else {
            //Draw image to click on to roll
            g2.drawImage(diceImage,
                    (int) ((screenWidth/2)-((100*scaleFactor)/2)),
                    (int) (screenHeight/1.3),
                    (int) (100*scaleFactor),
                    (int) (100*scaleFactor),
                    null);
            if (nextTurnAvailable) {
                //Draw image to click on to end your turn if its available
                g2.drawImage(nextTurnImage,
                        (int) ((screenWidth/2)-180*scaleFactor),
                        (int) (screenHeight/1.3),
                        (int) (100*scaleFactor),
                        (int) (100*scaleFactor),
                        null);
            } else {
                //Draw image to click on to end your turn if its unavailable
                g2.drawImage(nextTurnImageGrey,
                        (int) ((screenWidth/2)-180*scaleFactor),
                        (int) (screenHeight/1.3),
                        (int) (100*scaleFactor),
                        (int) (100*scaleFactor),
                        null);
            }
            if (displayDice) {
                if (diceCounter < 50) {
                   drawRandomDice(g2);
                }
                else {
                    //TODO: Change after adding the option to buy streets.
                    if (!nextTurnAvailable) {
                        nextTurnAvailable = true;
                        currentPlayer.move(finalRoll[0]+finalRoll[1], bp);
                    }
                    drawFinalDice(g2);
                }
            }
            if (streetInfo != null) {
                streetInfo.draw(g2);
            }
        }
    }

    public void nextTurn(int turn) {
        //Go to next Player
        currentPlayer = playerList.get(turn);

        //Reset Values
        diceCounter = 0;
        displayDice = false;
        nextTurn = false;
        nextTurnAvailable = false;
        streetInfo = null;
    }

    public void endSetup() {
        setup=false;
    }

    public void dispose() {
        bp.remove(rollButton);
        rollButton.removeActionListener(diceListener);
    }

    //Getters / Setters
    public int getWidth() {
        return screenWidth;
    }

    public int getHeight() {
        return screenHeight;
    }

    public boolean isNextTurn() {
        return nextTurn;
    }

    public boolean isSettingUp() {
        return setup;
    }

    public double getScaleFactor() { return scaleFactor; }

    public void setPlayerList(ArrayList<Player> pList) {
        playerList = pList;
        currentPlayer = pList.get(0);
    }



}
