package com.company;

import org.jetbrains.annotations.NotNull;

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
            if (!displayDice || (currentPlayer.isDoublets() && diceCounter >= 50)){
                diceCounter = 0;
                if (hasMoved) {

                    hasMoved = false;
                }
                finalRoll = currentPlayer.roll(bp);
                displayDice = true;
            }
        }
    };
    private final ActionListener nextListener = e -> {
        if (nextTurnAvailable) { nextTurn = true; }
    };


    private final int diceY;
    private final int diceFirstX;
    private final int diceSecondX;

    public UI(Board bp) {
        this.bp = bp;

        screenHeight = bp.getScreenHeight();
        screenWidth = bp.getScreenWidth();
        scaleFactor = screenHeight/1080.0;

        diceY= (int) (150*scaleFactor);
        diceFirstX = (int) (175*scaleFactor);
        diceSecondX= (int) (25*scaleFactor);

        setupui = new setupUI(this.bp,this);

        warningFont =  new Font("Roboto", Font.PLAIN, (int) (35 * scaleFactor));

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
        hasMoved  =false;
    }

    public void disableButtons(@NotNull ArrayList<JButton> btnList) {
        for(JButton btn: btnList) {
            bp.remove(btn);
        }
    }

    public void disableButtons(@NotNull JButton btn) {
        bp.remove(btn);
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

    private Info propertyInfo;
    public void drawInfo(Info info){
        propertyInfo = info;
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
        g2.drawImage(imgNumbers.get(firstDice-1), screenWidth/2-diceFirstX, diceY, null);
        g2.drawImage(imgNumbers.get(secondDice-1), screenWidth/2-diceSecondX, diceY, null);
        diceCounter += 1;
    }



    private void drawFinalDice(Graphics2D g2) {
        if (finalRoll[0] == finalRoll[1]) {
            g2.drawImage(imgNumbersGolden.get(finalRoll[0]-1), screenWidth/2-diceFirstX, diceY, null);
            g2.drawImage(imgNumbersGolden.get(finalRoll[1]-1), screenWidth/2-diceSecondX, diceY, null);
        } else {
            g2.drawImage(imgNumbers.get(finalRoll[0]-1), screenWidth/2-diceFirstX, diceY, null);
            g2.drawImage(imgNumbers.get(finalRoll[1]-1), screenWidth/2-diceSecondX, diceY, null);
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
                    if (!hasMoved) {
                        hasMoved = true;
                        if (!currentPlayer.isDoublets()) {
                            nextTurnAvailable = true;
                        }
                        System.out.println("NEW");
                        System.out.println(currentPlayer.getPosition());
                        currentPlayer.move(finalRoll[0]+finalRoll[1], bp);
                        System.out.println(currentPlayer.getPosition());

                    }
                    drawFinalDice(g2);
                }
            }
            if (propertyInfo != null) {
                propertyInfo.draw(g2);
            }
            if(pInfo != null){
                pInfo.draw(g2);
            }
            if (notEnoughMoney) {
                notEnoughMoney = drawWarning(g2, "NICHT GENÜGEND GELD!");
            }
            if (noMonopolyWarning) {
                noMonopolyWarning = drawWarning(g2, "SIE BESITZEN NICHT ALLE HÄUSER DIESER FARBE!");
            }
            if (propertyMortgagedWarning) {
                propertyMortgagedWarning = drawWarning(g2, "AUF IHREM GRUNDSTÜCK LIEGT EINE HYPOTHEK!");
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    private boolean hasMoved;
    private PrisonInfo pInfo;
    private boolean isInPrison;
    public void nextTurn(int turn) {
        //Go to next Player
        currentPlayer = playerList.get(turn);
        pInfo = null;
        //Reset Values
        diceCounter = 0;
        hasMoved = false;
        displayDice = false;
        nextTurn = false;
        nextTurnAvailable = false;
        propertyInfo = null;
        if (currentPlayer.isInPrison()){
            pInfo = new PrisonInfo((SpecialField) bp.getPropertyList(currentPlayer.getPosition()),this,bp,bp.getPrisonField().getPrice());
        }
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

    public void disableInfo() {
        disableButtons(propertyInfo.getButtons());
        propertyInfo = null;
        pInfo = null;
    }

    private boolean propertyMortgagedWarning = false;
    public void drawMortgageWarning() {
        propertyMortgagedWarning = true;
        warningDrawCount = 0;
    }

    private final Font warningFont;
    private int warningDrawCount = 0;
    private boolean notEnoughMoney = false;
    private boolean noMonopolyWarning = false;
    public void notEnoughMoneyWarning() {
        notEnoughMoney = true;
        warningDrawCount = 0;
    }
    
    private boolean drawWarning(Graphics2D g2, String warning) {
        //Increasing transparency with each iteration
        g2.setFont(warningFont);


        g2.setColor(new Color(0, 0, 0, 255 - warningDrawCount));
        g2.drawString(warning, (getWidth() / 2) - (utils.stringWidth(g2, warning) / 2), (getHeight()) / 2);
        warningDrawCount += 1;
        if (warningDrawCount > 255) {
            warningDrawCount = 0;
            return false;
        } else {
            return true;
        }
    }

    public void noMonopolyWarning() {
        noMonopolyWarning = true;
        warningDrawCount = 0;
    }
}
