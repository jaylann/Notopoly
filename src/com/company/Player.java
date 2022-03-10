package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Random;

public class Player {
    private final String name;
    private final Image character;
    private int money;
    private int position = 0;
    private ArrayList<Property> properties = new ArrayList<>();
    private boolean bankrupt = false;
    private int timeInPrison = 0;
    private boolean prison = false;
    private int doubletCount = 0;
    private int recentRoll;
    private final Image icon;
    private final Random rand = new Random();
    public Player(int startMoney, String playerName, Image playerCharacter, Image playerIcon) {
        money = startMoney;
        name = playerName;
        character = playerCharacter;
        money = 50000;
        this.icon = playerIcon;
    }

    public Image getIcon() {return icon;}

    public int getTimeInPrison() {
        return timeInPrison;
    }

    public int getRecentRoll() {
        return recentRoll;
    }

    public boolean doublets = false;

    public void setRecentRoll(int number) {
        recentRoll = number;
    }


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
            payMiddle(bp.getPrisonField().getPrice(),bp, true);
            setPrison(false);
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
        boolean hasFound = false;
        ArrayList<Property> tmp_list = new ArrayList<>();
        for (Property prop: properties) {
            hasFound = false;
            for (int i = 0; i<tmp_list.size(); i++) {
                if (prop.getID() >= tmp_list.get(tmp_list.size()-i-1).getID()) {
                    tmp_list.add(tmp_list.size()-i, prop);
                    hasFound = true;
                    break;
                }
            }
            if (!hasFound) {
                tmp_list.add(0,prop);
            }

        }
        this.properties = tmp_list;
        return tmp_list;
    }

    public Hashtable<Integer, ArrayList<Property>>getPropertiesSorted() {
        Hashtable<Integer, ArrayList<Property>> sortedPropertyList = new Hashtable<>();
        ArrayList<Integer> scannedIDs = new ArrayList<>();
        ArrayList<Property> tmpList = new ArrayList<>();
        for (Property p: properties) {
            if (!scannedIDs.contains(p.getID())) {
                for (Property scanProp: properties) {
                    if (scanProp.getID() == p.getID()) {
                        tmpList.add(scanProp);
                    }
                }
                scannedIDs.add(p.getID());
                sortedPropertyList.put(p.getID(), tmpList);
                tmpList = new ArrayList<>();
            }
        }
        return sortedPropertyList;
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
        System.out.println(this.money);
    }

    public String getName() {return name;}

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
        if ((money - amount) >= 0) {
            money = money - amount;
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
        doublets = false;
        doubletCount = 0;
        if (inPrison) {
            this.position = 10;
        }
    }

    public boolean buyProperty(Property boughtProperty) {
        if (removeMoney(boughtProperty.getBuyPrice())) {
            addProperty(boughtProperty);
            System.out.println("BOUGHT: " + boughtProperty.getName());
            return true;
        } else {
            System.out.println("DID NOT BUY");
            return false;
        }
    }

    public ArrayList<Street> getStreets() {
        ArrayList<Street> tmpList = new ArrayList<>();
        for (Property p: properties) {
            if (p.getClass() == Street.class) {
                tmpList.add((Street) p);
            }
        }
        return tmpList;
    }

    public void sellProperty(int price, Property soldProperty, Player buyer) {
        if (buyer.removeMoney(price)) {
            removeProperty(soldProperty);
            buyer.addProperty(soldProperty);
        }
    }

    //Make Private
    private void addProperty(Property property) {
        if (property.changeOwner(this)) {
            properties.add(property);
        } else {
            addMoney(property.getBuyPrice());
        }
        System.out.println(properties);
    }
    public void addProperty(Property property, boolean override) {
        property.changeOwner(this, true);
        properties.add(property);
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
    public boolean isInPrison(){return this.prison;}

    private int prisonFreeCards=0;

    public void addPrisonFree() {
        prisonFreeCards++;
    }

    public int getMoney() {
        return money;
    }


}
