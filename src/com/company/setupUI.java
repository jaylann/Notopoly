package com.company;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;


public class setupUI {

    private final Board bp;
    private final UI parentUI;

    private final JTextArea nameBox;

    //Screen Values
    private final int screenWidth;
    private final int screenHeight;
    private final double scaleFactor;

    //Figure Button list for easy access
    private final ArrayList<JButton> btnList = new ArrayList<>();

    //Figures Images
    private final ArrayList<Image> figures;

    private final Font textFont;

    private int pages;
    private int page = 0;

    //<editor-fold desc="Figure Values">
    private final int figureWidth;
    private final int figureHeight;
    private final int firstColumnX;
    private final int secondColumnX;
    private final int firstRowY;
    private final int secondRowY;
    private final int figureArc;
    //</editor-fold>

    //<editor-fold desc="Button Values">
    private final int buttonWidth;
    private final int buttonHeight;
    private final int buttonLeftX;
    private final int buttonRightX;
    private final int buttonY;
    //</editor-fold>

    //<editor-fold desc="Namebox Values">
    private final int nameBoxWidth;
    private final int nameBoxHeight;
    private final int nameBoxX;
    private final int nameBoxY;
    //</editor-fold>

    //<editor-fold desc="Chosen Figure Values">
    private final int chosenFigureWidth;
    private final int chosenFigureHeight;
    private final int chosenFigureX;
    private final int chosenFigureY;
    //</editor-fold>lll

    //<editor-fold desc="Chosen Figure Text Values">
    private final int chosenFigureTextX;
    private final int chosenFigureTextY;
    //</editor-fold>

    //<editor-fold desc="Outer Menu Values">
    private final int outerMenuWidth;
    private final int outerMenuHeight;
    private final int outerMenuX;
    private final int outerMenuY;
    private final int outerMenuArc;
    //</editor-fold>

    //<editor-fold desc="Figure Buttons">
    private final JButton figureButton1;
    private final JButton figureButton2;
    private final JButton figureButton3;
    private final JButton figureButton4;
    //</editor-fold>

    //<editor-fold desc="Control Buttons">
    private final JButton nextPageButton;
    private final JButton lastPageButton;
    private final JButton closePageButton;
    private final JButton acceptPageButton;
    private final JButton endSetupButton;
    //</editor-fold>

    //<editor-fold desc="Control Button Images">
    private final Image nextImg;
    private final Image closeImg;
    private final Image acceptImg;
    private final Image backImg;
    //</editor-fold>

    //<editor-fold desc="Colors">
    private final Color emptyTextColor = new Color(200, 200, 200, 180);
    private final Color bgColor = new Color(72, 72, 72, 200);
    private final Color menuColor = new Color(36, 36, 36, 150);
    //</editor-fold>

    //<editor-fold desc="Strokes">
    private final Stroke menuStroke = new BasicStroke(10);
    private final Stroke innerStroke = new BasicStroke(4);
    //</editor-fold>

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
    private final ActionListener endSetupListener = e -> {
        if (players.size() > 1) {
            end();
        }
    };
    //</editor-fold>

    private Image chosenChar;
    private final ActionListener charListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] coordinateString = e.getSource().toString().substring(21, 28).split(",", 2);

            int x = Integer.parseInt(coordinateString[0]);
            int y = Integer.parseInt(coordinateString[1]);

            if (x == firstColumnX) {
                x = 0;
            } else {
                x = 1;
            }

            if (y == firstRowY) {
                y = 0;
            } else {
                y = 2;
            }
            if ((y + x) + (page * 4) < figures.size()) {
                charClicked(figures.get((y + x) + (page * 4)));
            }
        }
    };
    private final ActionListener acceptListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenChar != null) {

                if (!nameBox.getText().isEmpty()) {
                    //Disabling buttons only usable in Chosen Char Window
                    bp.remove(closePageButton);
                    bp.remove(nameBox);
                    bp.remove(acceptPageButton);

                    //Enabling standard Buttons
                    for (JButton btn : btnList) {
                        bp.add(btn);
                    }
                    bp.add(nextPageButton);
                    bp.add(lastPageButton);

                    players.add(new Player(8000, nameBox.getText(), utils.loadImage(pathTable.get(chosenChar),
                            (int) (60*scaleFactor), (int) (60*scaleFactor)), chosenChar));

                    //Resetting values and removing chosenChar
                    figures.remove(chosenChar);
                    pages = (int) Math.ceil(figures.size() / 4.0);
                    page = 0;
                    chosenChar = null;
                    nameBox.setText("");

                    if (players.size() > 3) {
                        end();
                    }
                }
            }
        }
    };
    private final ActionListener closeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (chosenChar != null) {
                chosenChar = null;
                nameBox.setText("");

                bp.remove(closePageButton);
                bp.remove(nameBox);
                bp.remove(acceptPageButton);
                for (JButton btn : btnList) {
                    bp.add(btn);
                }

                bp.add(nextPageButton);
                bp.add(lastPageButton);
            }
        }
    };

    public setupUI(@NotNull Board bp, @NotNull UI parent) {
        this.bp = bp;

        //<editor-fold desc="Dependencies">
        screenWidth = parent.getWidth();
        screenHeight = parent.getWidth();
        scaleFactor = parent.getScaleFactor();
        parentUI = parent;
        //</editor-fold>

        //<editor-fold desc="Figure Values">
        figureWidth = (int) (200 * scaleFactor);
        figureHeight = (int) (200 * scaleFactor);
        firstColumnX = (int) (290 * scaleFactor);
        secondColumnX = (int) (590 * scaleFactor);
        firstRowY = (int) (320 * scaleFactor);
        secondRowY = (int) (590 * scaleFactor);
        figureArc = (int) (15 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Button Values">
        buttonWidth = (int) (75 * scaleFactor);
        buttonHeight = (int) (75 * scaleFactor);
        buttonLeftX = (int) (205 * scaleFactor);
        buttonRightX = (int) (800 * scaleFactor);
        buttonY = (int) (800 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Namebox Values">
        nameBoxWidth = (int) (480 * scaleFactor);
        nameBoxHeight = (int) (60 * scaleFactor);
        nameBoxX = (int) (300 * scaleFactor);
        nameBoxY = (int) (720 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Chosen Figure Values">
        chosenFigureWidth = (int) (480 * scaleFactor);
        chosenFigureHeight = (int) (480 * scaleFactor);
        chosenFigureX = (int) (300 * scaleFactor);
        chosenFigureY = (int) (260 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Chosen Figure Text Values">
        chosenFigureTextX = (int) (320 * scaleFactor);
        chosenFigureTextY = (int) (765 * scaleFactor);
        //</editor-fold>

        //<editor-fold desc="Outer Menu Values">
        outerMenuWidth = (int) (680 * scaleFactor);
        outerMenuHeight = (int) (680 * scaleFactor);
        outerMenuX = (int) (200 * scaleFactor);
        outerMenuY = (int) (200 * scaleFactor);
        outerMenuArc = (int) (50 * scaleFactor);
        //</editor-fold>

        //Loading all Figure Images
        figures = loadFigures(chosenFigureWidth, chosenFigureHeight);

        //<editor-fold desc="Figure Buttons">

        //Creating Figure Buttons
        figureButton1 = parentUI.createButton(figureWidth, figureHeight, firstColumnX, firstRowY, charListener);
        figureButton2 = parentUI.createButton(figureWidth, figureHeight, secondColumnX, secondRowY, charListener);
        figureButton3 = parentUI.createButton(figureWidth, figureHeight, firstColumnX, secondRowY, charListener);
        figureButton4 = parentUI.createButton(figureWidth, figureHeight, secondColumnX, firstRowY, charListener);

        //Adding Figure Buttons to btnList for later access
        btnList.add(figureButton1);
        btnList.add(figureButton2);
        btnList.add(figureButton3);
        btnList.add(figureButton4);

        //</editor-fold>

        //<editor-fold desc="Images">
        //Loading images for next page and such
        nextImg = utils.loadImage("images/next.png", buttonWidth, buttonHeight);
        closeImg = utils.loadImage("images/close.png", buttonWidth, buttonHeight);
        acceptImg = utils.loadImage("images/tick.png", buttonWidth, buttonHeight);
        backImg = utils.loadImage("images/back.png", buttonWidth, buttonHeight);
        //</editor-fold>

        //<editor-fold desc="Control Buttons">
        //Creating Control Buttons
        nextPageButton = parentUI.createButton(buttonWidth, buttonHeight, buttonRightX, buttonY, nextListener);
        lastPageButton = parentUI.createButton(buttonWidth, buttonHeight, buttonLeftX, buttonY, backListener);
        closePageButton = parentUI.createButton(buttonWidth, buttonHeight, buttonLeftX, buttonY, closeListener);
        acceptPageButton = parentUI.createButton(buttonWidth, buttonHeight, buttonRightX, buttonY, acceptListener);
        endSetupButton = parentUI.createButton(buttonWidth, buttonHeight, (screenWidth / 2) - (buttonWidth / 2), buttonY, endSetupListener);
        //</editor-fold>

        //Max amount of pages possible
        pages = (int) Math.ceil(figures.size() / 4.0);

        //Creating box to type Player name into
        nameBox = new JTextArea();
        nameBox.setEditable(true);
        nameBox.setBounds(nameBoxX, nameBoxY, nameBoxWidth, nameBoxHeight);
        nameBox.setOpaque(false);

        textFont = new Font("Monopoly", Font.PLAIN, (int) (46 * scaleFactor));
    }

    private void end() {
        bp.setPlayerList(players);

        parentUI.setPlayerList(players);
        parentUI.endSetup();

        disableButtons();
    }

    private void disableButtons() {
        //Removing all clickable Buttons
        for (JButton btn : btnList) {
            bp.remove(btn);
        }
        bp.remove(nextPageButton);
        bp.remove(lastPageButton);
        bp.remove(endSetupButton);
        bp.remove(nameBox);
        bp.remove(closePageButton);
        bp.remove(acceptPageButton);
    }
    //</editor-fold>

    private void charClicked(Image character) {
        chosenChar = character;

        //Disabling all Figure Buttons
        for (JButton btn : btnList) {
            bp.remove(btn);
        }
        bp.remove(nextPageButton);
        bp.remove(lastPageButton);

        //Enabling nameBox
        bp.add(nameBox);
        bp.add(closePageButton);
        bp.add(acceptPageButton);
    }

    private Hashtable<Image, String> pathTable = new Hashtable<>();

    private ArrayList<Image> loadFigures(int width, int height) {
        ArrayList<Image> loadList = new ArrayList<>();

        //Get all Files in Folder
        File folder = new File("images/figures/");
        File[] listOfFiles = folder.listFiles();

        //Loading all Files in Folder
        try {
            assert listOfFiles != null;
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    Image loadImage = utils.loadImage(listOfFile.getPath(), width, height);
                    loadList.add(loadImage);
                    pathTable.put(loadImage, listOfFile.getPath());
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return loadList;
    }
    //</editor-fold>

    public void draw(Graphics2D g2) {

        g2.setFont(textFont);

        //Creating semi-Transparent grey background
        g2.setColor(bgColor);
        g2.fillRect(0, 0, (int) (2000 * scaleFactor), (int) (2000 * scaleFactor));

        //Drawing outer menu box
        g2.setColor(Color.black);
        g2.setStroke(menuStroke);
        g2.drawRoundRect(outerMenuX, outerMenuY, outerMenuWidth, outerMenuHeight, outerMenuArc, outerMenuArc);

        //Filling outer menu box
        g2.setColor(menuColor);
        g2.fillRoundRect(outerMenuX, outerMenuY, outerMenuWidth, outerMenuHeight, outerMenuArc, outerMenuArc);

        //Setting Color white for text
        g2.setColor(Color.white);

        //Setting stroke for smaller inner boxes
        g2.setStroke(innerStroke);

        if (chosenChar == null) {
            int x, y, i, max;
            //Getting max available Figures on page
            if (page + 1 == pages) {
                max = (figures.size() % 4);
                if (max == 0) {
                    max = 4;
                }
            } else {
                max = 4;
            }
            //Iterating through all possible positions
            for (i = 0; i < max; i++) {
                if (i % 2 == 0) {
                    x = firstColumnX;
                } else {
                    x = secondColumnX;
                }
                if (i < 2) {
                    y = firstRowY;
                } else {
                    y = secondRowY;
                }

                //Drawing boxes around Figures
                g2.drawRoundRect(x, y, figureWidth, figureHeight, figureArc, figureArc);
                g2.drawRoundRect(x, y, figureWidth, figureHeight, figureArc, figureArc);
                g2.drawRoundRect(x, y, figureWidth, figureHeight, figureArc, figureArc);
                g2.drawRoundRect(x, y, figureWidth, figureHeight, figureArc, figureArc);

                //Drawing figures
                g2.drawImage(figures.get(i + (page * 4)), x, y, figureWidth, figureHeight, null);
            }
            if (players.size() > 1) {
                g2.drawImage(acceptImg, (screenWidth / 2) - (buttonWidth / 2), buttonY, buttonWidth, buttonHeight, null);
            }

            if (pages > (page + 1)) {
                g2.drawImage(nextImg, buttonRightX, buttonY, buttonWidth, buttonHeight, null);
            }

            if (page > 0) {
                g2.drawImage(backImg, buttonLeftX, buttonY, buttonWidth, buttonHeight, null);
            }

        } else {
            //Drawing big Figure if player has clicked on it
            g2.drawImage(chosenChar, chosenFigureX, chosenFigureY, chosenFigureWidth, chosenFigureHeight, null);
            g2.drawRoundRect(nameBoxX, nameBoxY, nameBoxWidth, nameBoxHeight, figureArc, figureArc);

            //Draw Control Buttons
            g2.drawImage(closeImg, buttonLeftX, buttonY, buttonWidth, buttonHeight, null);
            g2.drawImage(acceptImg, buttonRightX, buttonY, buttonWidth, buttonHeight, null);

            if (nameBox.getText().isEmpty()) {
                g2.setColor(emptyTextColor);
                g2.drawString("Cool Name...", chosenFigureTextX, chosenFigureTextY);
            } else {
                //Sanitizing String
                if (!nameBox.getText().matches("[A-Za-z0-9]+")) {
                    nameBox.setText(nameBox.getText().substring(0, nameBox.getText().length() - 1));
                }
                //Checking if string is longer than 24 characters
                if (g2.getFontMetrics().stringWidth(nameBox.getText()) > 440 * scaleFactor) {
                    //Removing last character
                    nameBox.setText(nameBox.getText().substring(0, nameBox.getText().length() - 1));
                }
                g2.setColor(Color.white);
                g2.drawString(nameBox.getText(), chosenFigureTextX, chosenFigureTextY);
            }
        }
        g2.setColor(Color.white);
        g2.drawString(String.format("PLAYER: %d", players.size() + 1), (screenWidth / 2) - (g2.getFontMetrics().stringWidth(String.format("PLAYER %d:", players.size() + 1)) / 2), (int) (270 * scaleFactor));
    }
}
