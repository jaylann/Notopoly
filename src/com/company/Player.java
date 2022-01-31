package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int money;
    private int position = 0;
    private ArrayList<Property> properties;
    private final String name;
    private final String character;
    private boolean bankrupt = false;

    public Player(int startMoney, String playerName, String playerCharacter) {
        money = startMoney;

        name = playerName;
        character = playerCharacter;
    }

    public int[] roll() {
        Random rand = new Random();
        int firstDice = rand.nextInt(1,7);
        int secondDice = rand.nextInt(1,7);
        System.out.println("Name: "+ name);
        System.out.println("First dice: " + firstDice + "    Second dice: " + secondDice);
        return new int[]{firstDice, secondDice};
    }
    public void removeMoney(int amount) {
        if (money-amount > 0) {
            money -= amount;
        }
        else {bankrupt = true;}
    }
    public void addMoney(int amount) {
        money+=amount;
    }

}
