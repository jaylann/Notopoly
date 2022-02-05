package com.company;

import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setTitle("Notopoly");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension size = new Dimension(1920,1080);
        double scaleFactorWidth = 2560/1080.0;
        double scaleFactorHeight = 1440/1080.0;

        window.setSize((int) (size.getWidth()/scaleFactorWidth), (int) (size.getHeight()/scaleFactorHeight));
        //window.setSize(1057, 1080);

        Board board = new Board(window);

        window.add(board);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        board.startGameThread();
    }




}
