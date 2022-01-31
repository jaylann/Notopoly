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

    private Image dice;
    private JButton rollButton;

    private final ArrayList<Player> playerList;
    private Player currentPlayer;
    
    private final double scaleFactorWidth;
    private final double scaleFactorHeight;

    private final int screenWidth;
    private final int screenHeight;

    private int[] finalRoll;
    private boolean displayDice = false;

    private final ActionListener diceListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("YOU CLICKED ME");
            finalRoll = currentPlayer.roll();
            displayDice = true;
            //nextTurn = true;
        }
    };


    public UI(Board bp, ArrayList<Player> pList) {



        this.bp = bp;

        screenHeight = bp.getScreenHeight();
        screenWidth = bp.getScreenWidth();

        scaleFactorWidth = 1057.0/screenWidth;
        scaleFactorHeight = 1080.0/screenHeight;

        currentPlayer = pList.get(0);
        playerList = pList;

        //Loading Images
        try {
            this.dice = ImageIO.read(new File("images/dice.png")).getScaledInstance(100,100, Image.SCALE_SMOOTH);
            //TODO: Add other images for buttons
        } catch (IOException ignored) {
            //TODO: Handle exception instead of just ignoring it
        }

        rollButton = createButton(dice,
                (int) (100*scaleFactorWidth),
                (int) (100*scaleFactorHeight),
                (int) ((screenWidth/2)-60*scaleFactorWidth),
                (int) (screenHeight/1.35),
                diceListener);

        //TODO: Add different buttons
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
    private String firstDiceString = String.format("%d", rand.nextInt(1, 7));
    private String secondDiceString = String.format("%d", rand.nextInt(1, 7));

    private int diceCounter;

    public void draw(Graphics2D g2) {
        g2.drawImage(dice,
                (int) ((screenWidth/2)-60*scaleFactorWidth),
                (int) (screenHeight/1.35),
                (int) (100*scaleFactorWidth),
                (int) (100*scaleFactorHeight),
                null);
        if (displayDice) {
            if (diceCounter < 151) {

                if (diceCounter % 5 == 0) {
                    firstDiceString = String.format("%d", rand.nextInt(1, 7));
                    secondDiceString = String.format("%d", rand.nextInt(1, 7));

                }
                //TODO: Add proper numbers and positions
                g2.drawString(firstDiceString, 500, 500);
                g2.drawString(secondDiceString, 550, 550);
                diceCounter += 1;
            }
            else {
                //TODO: See above
                g2.drawString(String.format("%d",finalRoll[0]),500,500);
                g2.drawString(String.format("%d",finalRoll[1]),550,550);
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
    }
    public void dispose() {
        bp.remove(rollButton);
        rollButton.removeActionListener(diceListener);
    }


}
