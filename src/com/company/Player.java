package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final String name;
    private final Image character;
    private int money;
    private int position = 0;
    private final ArrayList<Property> properties = new ArrayList<>();
    private boolean bankrupt = false;
    private int timeInPrison = 0;
    private boolean prison = false;
    private int doubletCount = 0;
    private int recentRoll;
    private final Random rand = new Random();
    public Player(int startMoney, String playerName, Image playerCharacter) {
        money = startMoney;
        name = playerName;
        character = playerCharacter;
    }

    public int getRecentRoll() {
        return recentRoll;
    }

    public boolean doublets = false;

    public int[] roll(Board bp) {

        int firstDice = rand.nextInt(1, 7);
        int secondDice = rand.nextInt(1, 7);
        System.out.println("Name: " + name);
        System.out.println("First dice: " + firstDice + "    Second dice: " + secondDice);
        if (firstDice == secondDice) {
            doublets = true;
            doubletCount++;
            if (this.isInPrison()) {
                setPrison(false);
            }
        } else {
            doublets = false;
            doubletCount = 0;
        }
        if(timeInPrison >= 3) {
            setPrison(false);
            payMiddle(bp.getPrisonField().getPrice(),bp, true);
        }
        if (doubletCount>=3) {
            setPrison(true);
        }
        recentRoll = firstDice+secondDice;
        return new int[]{firstDice, secondDice};
    }

    public boolean isDoublets() {
        return doublets;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }
    public void move(int fields, Board bp) {

        if (!isInPrison()) {
            if (this.position + fields > 39) {
                this.position += fields - 40;
                if (this.position > 0) {
                    Go.passBy(this);
                }
            } else {
                this.position += fields;
            }
            landOn(bp.getPropertyList(getPosition()));
        } else {
            timeInPrison++;
        }

    }
    public void landOn(Field landed) {
        landed.landOn(this);
    }
    public int removeMoney(int amount, boolean forced) {

        if (!forced) {
            if (money - amount >= 0) {
                money -= amount;
                return amount;
            } else {
                return 0;
            }
        } else {
            if (money - amount < 0) {
                money = 0;
                bankrupt = true;
                return amount + (money - amount);
            } else {
                money -= amount;
                return amount;
            }
        }
    }

    public boolean removeMoney(int amount) {
        if (money - amount >= 0) {
            money -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void setPrison(boolean inPrison) {
        this.prison = inPrison;
        if (inPrison) {
            this.position = 10;
        }
    }

    public boolean buyProperty(int price, Property boughtProperty) {
        if (removeMoney(price)) {
            addProperty(boughtProperty);
            return true;
        } else {
            return false;
        }
    }

    public void sellProperty(int price, Property soldProperty, Player buyer) {
        if (buyer.removeMoney(price)) {
            removeProperty(soldProperty);
            buyer.addProperty(soldProperty);
        }
    }

    private void addProperty(Property property) {
        if (property.changeOwner(this)) {
            properties.add(property);
        } else {
            addMoney(property.getBuyPrice());
        }
        System.out.println(properties);
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

    public void setPosition(int pos) {
        this.position = pos;
    }

    public boolean payMiddle(int amount, Board bp, boolean forced) {
        int paidMoney = removeMoney(amount, forced);
        bp.addMiddleMoney(paidMoney);
        return paidMoney == amount || forced;

    }
    public boolean isInPrison(){return prison;}

    private int prisonFreeCards=0;

    public void addPrisonFree() {
        prisonFreeCards++;
    }
}
