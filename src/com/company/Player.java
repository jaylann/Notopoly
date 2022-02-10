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
    private boolean prison = false;

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

    public void move(int fields, Board bp) {
        if (this.position + fields > 39) {
            this.position += fields -40;
        } else {

        this.position += fields;
        }
        bp.getPropertyList(this.position).landOn(this);
    }

    public boolean removeMoney(int amount, boolean forced) {
        if (money-amount > 0 && !forced) {
            money -= amount;
            return true;
        } else if (forced) {
            if (money-amount < 0) {bankrupt=true;}
            return true;
        }
        else {return false;}
    }
    public void addMoney(int amount) {
        money+=amount;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public void setPrison(boolean inPrison) {
        this.prison = inPrison;
    }

    public void buyProperty(int price, Property boughtProperty) {
        if (removeMoney(price, false)) {
            addProperty(boughtProperty);
        }
    }
    public void sellProperty(int price, Property soldProperty, Player buyer) {
        if (buyer.removeMoney(price, false)) {
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
    public Image getCharacter() {
        return character;
    }
    public int getPosition() {
        return position;
    }
}
