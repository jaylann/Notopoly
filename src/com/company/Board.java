package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Board extends JPanel implements  Runnable{

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private final int screenWidth;
    private final int screenHeight;

    private ArrayList<Player> playerList = null;
    private final Image board;
    private final int FPS = 60;
    private final UI ui;
    private int turn;
    private int middleMoney = 0;

    private ArrayList<Property> propertyList = new ArrayList<>();

    private final double scaleFactor;


    Thread gameThread;

    private ArrayList<String[]> loadStreets() {
        ArrayList<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("streets.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                records.add((values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }


    private final int occupantWidth; 
    private final int occupantHeight; 
    private final int occupantSecondRowX; 
    private final int occupantSecondRowY; 
    private final int occupantFirstRowX; 
    private final int occupantFirstRowY; 
    
    public Board(JFrame frame) {
        String[] nameList = {"Los", "Badstrasse", "Gemeinschaftsfeld", "Turmstrasse", "Einkommensteuer", "Südbahnhof",
                "Chausseestrasse", "Ereignisfeld", "Elisenstrasse", "Poststrasse", "Gefängnis", "Seestrasse",
                "Elektrizitätswerk", "Hafenstrasse", "Neue-Strasse", "Westbahnhof", "Münchner-Strasse","Gemeinschaftsfeld",
                "Wiener-Strasse","Berliner-Strasse", "Frei-Parken", "Theaterstrasse", "Ereignisfeld", "Museumstrasse","Opernplatz",
                "Nordbahnhof", "Lessingstrasse", "Schillerstrasse", "Wasserwerk", "Goethestrasse", "Gehe-in-das-Gefängnis",
                "Rathausplatz", "Hauptstrasse", "Gemeinschaftsfeld", "Bahnhofstrasse", "Hauptbahnhof", "Zusatzsteuer",
                "Parkstrasse", "Ereignisfeld", "Schlossallee"};
        Color[] colorList = {new Color(156, 94, 50),new Color(97, 197, 255),new Color(255, 0, 234),new Color(255, 136, 0),new Color(255, 0, 0),new Color(255, 242, 0),new Color(0, 145,0),new Color(0, 6, 186)};
        int[] colorIndex = {0,0,1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7};
        int[] typeList = {6,0,2,0,5,1,0,3,0,0,7,0,4,0,0,1,0,2,0,0,8,0,3,0,0,1,0,0,4,0,9,0,0,2,0,1,5,0,3,0};
        Hashtable<String, Hashtable<String, Integer>> streetDict = new Hashtable<>();
        ArrayList<String> valueList = new ArrayList<>(Arrays.asList("price", "housePrice", "rent0", "rent1", "rent2", "rent3", "rent4", "rent5"));;
        ArrayList<String[]> csvFile = loadStreets();
        csvFile.remove(0);
        for (String[] s: csvFile) {
            Hashtable<String, Integer> tmpDict = new Hashtable<>();
            //System.out.println(Arrays.toString(s));
            for (int i = 1; i < s.length-1; i++) {
                tmpDict.put(valueList.get(i-1),Integer.parseInt(s[i]));
            }
            streetDict.put(s[0], tmpDict);
        }
        this.setLayout(null);
        screenHeight = frame.getHeight();
        screenWidth = frame.getWidth();
        System.out.println(screenHeight);
        System.out.println(screenWidth);
        scaleFactor = frame.getWidth()/1080.0;
        System.out.println(scaleFactor);


        occupantWidth = (int) (60*scaleFactor);
        occupantHeight = (int) (60*scaleFactor);
        occupantFirstRowX = (int) (40*scaleFactor);
        occupantFirstRowY = (int) (40*scaleFactor);
        occupantSecondRowX = (int) (100*scaleFactor);
        occupantSecondRowY = (int) (100*scaleFactor);
        
        
        
        board = utils.loadImage("images/board.png", (int) (1080*scaleFactor), (int) (1080*scaleFactor));
        ui = new UI(this);
        int z = 0;
        for (int i = 0; i < typeList.length; i++) {
            if (typeList[i] == 0) {
                //System.out.println(streetDict.get(nameList[i]));
                //System.out.println(i);
                propertyList.add(new Street(nameList[i], streetDict.get(nameList[i]), ui, colorList[colorIndex[z]]));
                z++;
            }
            else {
                propertyList.add(null);
            }
        }
    }

    private ArrayList<Property> createBoard() {

        return null;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private ArrayList<Player> getOccupants(int position) {
        ArrayList<Player> occupants = new ArrayList<>();
        for (Player p: playerList) {
            if (p.getPosition() == position) {
                occupants.add(p);
            }
        }
        return occupants;
    }

    public int getMiddleMoney() {
        return middleMoney;
    }
    public void setMiddleMoney(int money) {
        middleMoney = money;
    }
    public void addMiddleMoney(int money) {
        middleMoney += money;
    }




    private void drawPlayers(Graphics2D g2) {

        for (int i = 0; i < 40; i++) {
            ArrayList<Player> occupants = getOccupants(i);
            for (int j = 0; j < occupants.size(); j++) {
                int x = 0;
                int y = 0;

                int xOffset;
                int yOffset;

                double corner = i / 10.0;
                if (i % 10 == 0) {

                    switch ((int) corner) {
                        case 0 -> {
                            x = (int) (-140*scaleFactor);
                            y = (int) (-140*scaleFactor);
                        }
                        case 1 -> {
                            x = getScreenWidth()*-1;
                            y = (int) (-140*scaleFactor);
                        }
                        case 2 -> {
                            x = getScreenWidth()*-1;
                            y = getScreenHeight()*-1;
                        }
                        case 3 -> {
                            x = (int) (-140*scaleFactor);
                            y = getScreenHeight()*-1;
                        }
                    }
                    //TODO: Make it work with 1 player
                    final int xCornerOffset = (int) (x + (occupantWidth * j) + (15*scaleFactor));
                    switch (occupants.size()) {
                        case 1 -> {
                            switch ((int) corner) {
                                
                                case 0 -> g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()-occupantSecondRowX,getScreenHeight()-occupantSecondRowY,occupantWidth,occupantHeight,null);
                                case 1 -> g2.drawImage(occupants.get(j).getCharacter(), occupantFirstRowX,getScreenHeight()-occupantSecondRowY,occupantWidth,occupantHeight,null);
                                case 2 -> g2.drawImage(occupants.get(j).getCharacter(), occupantFirstRowX,occupantFirstRowY,occupantWidth,occupantHeight,null);
                                case 3 -> g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()-occupantSecondRowX,occupantFirstRowY,occupantWidth,occupantHeight,null);
                            }
                        }
                        case 2 -> {
                            xOffset = xCornerOffset;
                            yOffset = (int) (y+(35*scaleFactor));
                            g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()+xOffset, getScreenHeight()+yOffset, occupantWidth,occupantHeight,null);
                        }
                        case 3 -> {
                            xOffset = xCornerOffset;
                            yOffset = (int) (y+(10*scaleFactor));
                            if (j==2) {
                                g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()+x+occupantFirstRowX, getScreenHeight()+yOffset+occupantHeight, occupantWidth,occupantHeight,null);
                            } else {
                                g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()+xOffset, getScreenHeight()+yOffset, occupantWidth,occupantHeight,null);
                            }
                        }
                        case 4 -> {
                            if (j%2 == 0) {
                                x += 15*scaleFactor;
                            } else{
                                x += 75*scaleFactor;
                            }
                            if (j<2) {
                                y += 10*scaleFactor;
                            } else {
                                y  += 70*scaleFactor;
                            }
                            g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()+x, getScreenHeight()+y, occupantWidth,occupantHeight,null);
                        }
                    }
                } else {
                    final int firstFieldColumn = (int) ((150+(90*((i%10)-1)))*scaleFactor);
                    final int secondFieldColumn = (int) ((210+(90*((i%10)-1)))*scaleFactor);

                    if (corner < 1.0) {
                        x = secondFieldColumn;
                        if (occupants.size() > 1) {
                            if (j%2==0) {
                                y = 2*occupantHeight;
                            } else {
                                y = occupantHeight;
                            }
                        } else {
                            y = (int) (1.5*occupantHeight);
                        }
                        g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()-x, getScreenHeight()-y,occupantWidth,occupantHeight,null);
                    } else if (corner < 2.0) {
                        y = secondFieldColumn;
                        if (occupants.size() > 1) {
                            if (j%2==0) {
                                x = 0;
                            } else {
                                x = occupantWidth;
                            }
                        } else {
                            x = occupantWidth/3;
                        }
                        g2.drawImage(occupants.get(j).getCharacter(), x, getScreenHeight()-y,occupantWidth,occupantHeight,null);
                    } else if (corner < 3.0 ) {
                        x = (int) firstFieldColumn;
                        if (occupants.size() > 1) {
                            if (j%2==0) {
                                y = 0;
                            } else {
                                y = occupantHeight;
                            }
                        } else {
                            y = occupantWidth/3;
                        }
                        g2.drawImage(occupants.get(j).getCharacter(), x, y,occupantWidth,occupantHeight,null);
                    } else {
                        y = firstFieldColumn;
                        if (occupants.size() > 1) {
                            if (j%2==0) {
                                x = 2*occupantWidth;
                            } else {
                                x = occupantWidth;
                            }
                        } else {
                            x = (int) (1.5*occupantWidth);
                        }
                        g2.drawImage(occupants.get(j).getCharacter(), getScreenWidth()-x, y,occupantWidth,occupantHeight,null);
                    }
                }
            }
        }
    }
    @Override
    public void run() {

        double drawInterval = 1000000000.0/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            //Calculating the time delta
            delta += (currentTime- lastTime) / drawInterval;
            timer += (currentTime-lastTime);
            lastTime = currentTime;

            //If appropriate time for 60FPS has passed
            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            //If 1 second has passed
            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void setPlayerList(ArrayList<Player> pList) {
        playerList = pList;
    }

    public void update() {
        if (ui.isNextTurn()) {
            turn += 1;
            if (turn > playerList.size()-1) {turn = 0;}
            ui.nextTurn(turn);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //Enabling Anti-Aliasing and setting Priority to Render Quality
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint( RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY );

        g2.drawImage(board, 0,0,null);
        g2.setColor(Color.black);

        ui.draw(g2);
        if (playerList != null) {
            drawPlayers(g2);
        }

        g2.dispose();

    }

    public Property getPropertyList(int position) {
        return propertyList.get(position);
    }
}

