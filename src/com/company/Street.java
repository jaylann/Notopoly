package com.company;

import com.company.exceptions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Street extends Property {
    private int houses = 0;
    private final int housePrice;

    public ArrayList<Integer> getPriceList() {
        return priceList;
    }

    private final ArrayList<Integer> priceList;
    private boolean monopoly = false;
    private int rent;
    private final UI parentUI;
    private final Color color;
    private final int maxStreets;

    public int getRent() {
        return priceList.get(houses);
    }

    public Color getColor() {
        return color;
    }

    public Street(String sName, int price, ArrayList<Integer> pay, int hPrice, UI pUI, Color streetColor, int maxStreets, int propertyIndex) {
        super(sName,price, propertyIndex);
        priceList = pay;
        housePrice = hPrice;
        parentUI = pUI;
        color = streetColor;
        this.maxStreets = maxStreets;
    }

    public Street(String sName, Hashtable<String, Integer> values, UI pUI, Color streetColor, int maxStreets, int propertyIndex) {
        super(sName, values.get("price"), propertyIndex);
        housePrice = values.get("housePrice");
        priceList = new ArrayList<>(Arrays.asList(values.get("rent0"),values.get("rent1"),values.get("rent2"),values.get("rent3"),values.get("rent4"),values.get("rent5")));
        parentUI = pUI;
        color = streetColor;
        this.maxStreets = maxStreets;
    }

    public int getHousePrice() {
        return housePrice;
    }

    @Override
    protected void mortgage() throws alreadyMortgagedException, cannotMortgageHousedPropertyException {
        if (!mortgage && houses == 0) {
            mortgage = true;
            owner.addMoney(sellPrice /2);
        }
        else if (mortgage) { throw new alreadyMortgagedException(String.format("Cannot mortgage already mortgaged property: %s", name)); }
        else if (houses > 0) { throw new cannotMortgageHousedPropertyException(String.format("Cannot mortgage property: %s that contains: %d houses ", name, houses)); }
    }

    public void buyHouse() throws maxHousesPerPropertyReachedException {
        if(monopolyCheck() && limitCheck(houses)) {
            if (houses + 1 <= 5) {
                if (owner.removeMoney(housePrice)) {
                    houses++;
                    rent = priceList.get(houses);
                } else {
                    parentUI.notEnoughMoneyWarning();
                }
            }
            else { throw new maxHousesPerPropertyReachedException(String.format("Amount of houses on Street '%s' exceeds maximum of 5", name)
            ); }
        }
        else {
            parentUI.noMonopolyWarning();
        }

    }

    private boolean limitCheck(int houses) {
        int other_houses=0;
        for (Street street: getBlock()) {
            if (street.getHouses()>other_houses) {
                other_houses = street.getHouses();
            }
        }
        return Math.abs(houses - other_houses) <= 1;
    }

    public void sellHouse(int amount) throws negativeHousesException {
        if (houses - amount >= 0) {
            houses -= amount;
            owner.addMoney(amount*(housePrice/2));
            rent = priceList.get(houses);
        }
        else { throw new negativeHousesException(String.format("%d is not a valid Value for amount of houses for property: %s", houses-amount, name)); }
    }


    public boolean monopolyCheck() {
        monopoly = getBlock().size() == maxStreets;
        return monopoly;
    }

    public int getHouses() {
        return houses;
    }

    private ArrayList<Street> getBlock() {
        ArrayList<Street> blockList = new ArrayList<>();
        for(Property prop: owner.getProperties()) {
            if (prop.getClass() == Street.class) {
                if (((Street) prop).getColor() == this.color) {
                    blockList.add((Street) prop);
                }
            }
        }
        return blockList;
    }

    public void landOn(Player p) {
        if (!p.equals(owner) && owner != null) {

            int payment = rent;
            if (monopolyCheck()) {
                payment = 2*rent;
            }

            try {
                pay(p, payment);
            } catch (noOwnerException e) {
                e.printStackTrace();
            } catch (propertyMortgagedException e) {
                parentUI.drawMortgageWarning();
            }
        } else if (owner == null) {
            StreetInfo streetInfo = new StreetInfo(this, parentUI);
            parentUI.drawInfo(streetInfo);
        }
    }

}
