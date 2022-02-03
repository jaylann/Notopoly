package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Board extends JPanel implements  Runnable{

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    private final int screenWidth;
    private final int screenHeight;

    private final ArrayList<Player> playerList;
    private final JFrame jframe;
    private final Image board;
    private final int FPS = 60;
    private final UI ui;
    private int turn;


    Thread gameThread;

    public Board(JFrame frame, ArrayList<Player> players) {
        this.setLayout(null);
        // Reading board image
        try {
            board = ImageIO.read(new File("images/board.png")).getScaledInstance(1041,1041,Image.SCALE_SMOOTH);
        } catch (IOException e) { throw new RuntimeException("This file should always exist. Unless someone intentionally deleted it.", e); }

        playerList = players;
        jframe = frame;

        screenHeight = frame.getHeight();
        screenWidth = frame.getWidth();

        ui = new UI(this, playerList);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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

    public void update() {
        if (ui.isNextTurn()) {
            turn += 1;
            if (turn > 3) {turn = 0;}
            ui.nextTurn(turn);
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(board, 0,0,null);
        g2.setColor(Color.black);
        ui.draw(g2);

        g2.dispose();

    }

}

