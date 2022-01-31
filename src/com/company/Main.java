package com.company;

import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Notopoly");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleFactorWidth = 2560.0/1057.0;
        double scaleFactorHeight = 1440.0/1080.0;
        window.setSize((int) (size.getWidth()/scaleFactorWidth), (int) (size.getHeight()/scaleFactorHeight));

        ArrayList<Player> playerList = new ArrayList<Player>();
        Player p1 = new Player(9000,"test1","test");
        Player p2 = new Player(9000,"test2","test");
        Player p3 = new Player(9000,"test3","test");
        Player p4 = new Player(9000,"test4","test");
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);
        playerList.add(p4);
        Board board = new Board(window, playerList);

        window.add(board);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        board.startGameThread();
    }


}
