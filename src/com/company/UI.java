package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class UI {

    private final Board bp;
    private boolean nextTurn = false;

    private final Image diceImage;
    private final JButton rollButton;
    private final JButton nextButton;

    private final Image nextTurnImage;
    private final Image nextTurnImageGrey;

    private final ArrayList<Player> playerList;
    private Player currentPlayer;

    private final int screenWidth;
    private final int screenHeight;
    private final double scaleFactor;

    private final ArrayList<Image> imgNumbers;
    private final ArrayList<Image> imgNumbersGolden;

    private boolean nextTurnAvailable = false;
    private int[] finalRoll;
    private boolean displayDice = false;

    private final ActionListener diceListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            finalRoll = currentPlayer.roll();
            displayDice = true;
            nextTurnAvailable = true;
        }
    };

    private final ActionListener nextListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (nextTurnAvailable) { nextTurn = true; }
        }
    };

    public UI(Board bp, ArrayList<Player> pList) {
        this.bp = bp;

        screenHeight = bp.getScreenHeight();
        screenWidth = bp.getScreenWidth();
        scaleFactor = 1080.0/screenHeight;

        currentPlayer = pList.get(0);
        playerList = pList;

        //Loading Images
        diceImage = loadImage("images/dice.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        nextTurnImage = loadImage("images/nextArrow.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        nextTurnImageGrey = loadImage("images/nextArrowGrey.png", (int) (100*scaleFactor), (int) (100*scaleFactor));
        imgNumbers = loadNumberImages((int) (200*scaleFactor),(int) (200*scaleFactor), false);
        imgNumbersGolden = loadNumberImages((int) (200*scaleFactor), (int) (200*scaleFactor), true);

        rollButton = createButton(diceImage,
                (int) (100*scaleFactor),
                (int) (100*scaleFactor),
                (int) ((screenWidth/2)-60*scaleFactor),
                (int) (screenHeight/1.35),
                diceListener);
        nextButton = createButton(nextTurnImage,
                (int) (100*scaleFactor),
                (int) (100*scaleFactor),
                (int) ((screenWidth/2)-180*scaleFactor),
                (int) (screenHeight/1.35), nextListener);
        //TODO: Add different buttons
    }

    private Image loadImage(String path, int width, int height) {
        try {
            return ImageIO.read(new File(path)).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) { throw new RuntimeException("This file should always exist. Unless someone intentionally deleted it.",e); }
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
            loadList.add(loadImage(path, width,height));
        }
        return loadList;
    }

    private JButton createButton(Image btnImage, int width, int height, int x, int y, ActionListener btnListener) {
        JButton newButton = new JButton(new ImageIcon(btnImage));
        newButton.setLocation(x, y);
        newButton.setSize(width, height);
        newButton.setVisible(true);

        //Making button Invisible
        newButton.setOpaque(false);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);

        newButton.addActionListener(btnListener);
        //Adding button to board
        bp.add(newButton);
        return newButton;
    }

    //Generating first random numbers to display when rolling
    Random rand = new Random();
    private int firstDice = rand.nextInt(1, 7);
    private int secondDice = rand.nextInt(1, 7);

    private int diceCounter;

    public void draw(Graphics2D g2) {
        g2.drawImage(diceImage,
                (int) ((screenWidth/2)-60*scaleFactor),
                (int) (screenHeight/1.35),
                (int) (100*scaleFactor),
                (int) (100*scaleFactor),
                null);
        if (nextTurnAvailable) {
            g2.drawImage(nextTurnImage,
                    (int) ((screenWidth/2)-180*scaleFactor),
                    (int) (screenHeight/1.35),
                    (int) (100*scaleFactor),
                    (int) (100*scaleFactor),
                    null);
        } else {
            g2.drawImage(nextTurnImageGrey,
                    (int) ((screenWidth/2)-180*scaleFactor),
                    (int) (screenHeight/1.35),
                    (int) (100*scaleFactor),
                    (int) (100*scaleFactor),
                    null);
        }
        if (displayDice) {
            if (diceCounter < 50) {
                if (diceCounter % 5 == 0) {
                    firstDice = rand.nextInt(1, 7);
                    secondDice = rand.nextInt(1, 7);

                    int tmpInt = rand.nextInt(1, 7);
                    if (tmpInt != firstDice) {
                        firstDice = tmpInt;
                    } else if (tmpInt+1 < 7) {
                        firstDice = tmpInt + 1;
                    } else {firstDice = tmpInt - 1;}

                    tmpInt = rand.nextInt(1, 7);
                    if (tmpInt != secondDice) {
                        secondDice = tmpInt;
                    } else if (tmpInt+1 < 7) {
                        secondDice = tmpInt + 1;
                    } else {secondDice = tmpInt - 1;}
                }
                g2.drawImage(imgNumbers.get(firstDice-1), screenWidth/2-175, 250, null);
                g2.drawImage(imgNumbers.get(secondDice-1), screenWidth/2-25, 250, null);
                diceCounter += 1;
            }
            else {
                if (finalRoll[0] == finalRoll[1]) {
                    g2.drawImage(imgNumbersGolden.get(finalRoll[0]-1), screenWidth/2-175, 250, null);
                    g2.drawImage(imgNumbersGolden.get(finalRoll[1]-1), screenWidth/2-25, 250, null);
                } else {
                    g2.drawImage(imgNumbers.get(finalRoll[0]-1), screenWidth/2-175, 250, null);
                    g2.drawImage(imgNumbers.get(finalRoll[1]-1), screenWidth/2-25, 250, null);
                }
            }
        }
    }

    public boolean isNextTurn() {
        return nextTurn;
    }

    public void nextTurn(int turn) {
        nextTurn = false;
        currentPlayer = playerList.get(turn);
        displayDice = false;
        diceCounter = 0;
        nextTurnAvailable = false;
    }

    public void dispose() {
        bp.remove(rollButton);
        rollButton.removeActionListener(diceListener);
    }


}
