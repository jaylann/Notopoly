package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private int money;
    private int position = 0;
    private ArrayList<Property> properties;
    private final String name;
    private final Image character;
    private boolean bankrupt = false;

    public Player(int startMoney, String playerName, Image playerCharacter) {
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
    public boolean removeMoney(int amount) {
        if (money-amount > 0) {
            money -= amount;
            return true;
        }
        else {return false;}
    }
    public void addMoney(int amount) {
        money+=amount;
    }

    public void buyProperty(int price, Property boughtProperty) {
        if (removeMoney(price)) {
            addProperty(boughtProperty);
        }
    }
    public void sellProperty(int price, Property soldProperty, Player buyer) {
        if (buyer.removeMoney(price)) {
            removeProperty(soldProperty);
            buyer.addProperty(soldProperty);
        }
    }
    private void addProperty(Property property) {
        properties.add(property);
        property.changeOwner(this);
    }
    private void removeProperty(Property property) {
        properties.remove(property);
        property.changeOwner(null);
    }
}
