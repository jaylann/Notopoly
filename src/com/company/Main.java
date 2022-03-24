package com.company;

import javax.swing.JFrame;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setTitle("Notopoly");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension size = new Dimension(1560,1080);
        double scaleFactor = 1440/1080.0;

        window.setSize((int) (size.getHeight()/scaleFactor), (int) (size.getHeight()/scaleFactor));
        //window.setSize(1080, 1080);

        Board board = new Board(window);

        window.add(board);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        board.startGameThread();
    }




}
