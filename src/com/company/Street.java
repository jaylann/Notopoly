package com.company;

import com.company.exceptions.alreadyMortgagedException;
import com.company.exceptions.cannotMortgageHousedPropertyException;
import com.company.exceptions.maxHousesPerPropertyReachedException;
import com.company.exceptions.negativeHousesException;

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


    public Color getColor() {
        return color;
    }

    public Street(String sName, int price, ArrayList<Integer> pay, int hPrice, UI pUI, Color streetColor) {
        super(sName,price);
        priceList = pay;
        housePrice = hPrice;
        parentUI = pUI;
        color = streetColor;
    }

    public Street(String sName, Hashtable<String, Integer> values, UI pUI, Color streetColor) {
        super(sName, values.get("price"));
        housePrice = values.get("housePrice");
        priceList = new ArrayList<>(Arrays.asList(values.get("rent0"),values.get("rent1"),values.get("rent2"),values.get("rent3"),values.get("rent4"),values.get("rent5")));
        parentUI = pUI;
        color = streetColor;
    }

    public int getHousePrice() {
        return housePrice;
    }

    @Override
    protected void mortgage() throws alreadyMortgagedException, cannotMortgageHousedPropertyException {
        if (!mortgage && houses == 0) {
            mortgage = true;
            owner.addMoney((int) sellPrice/2);
        }
        else if (mortgage) { throw new alreadyMortgagedException(String.format("Cannot mortgage already mortgaged property: %s", name)); }
        else if (houses > 0) { throw new cannotMortgageHousedPropertyException(String.format("Cannot mortgage property: %s that contains: %d houses ", name, houses)); }
    }

    public void buildHouse(int amount) throws maxHousesPerPropertyReachedException {
        if (houses + amount < 5) {
            houses += amount;
            owner.removeMoney(amount*housePrice, false);
            rent = priceList.get(houses);
        }
        else { throw new maxHousesPerPropertyReachedException(String.format("Amount of houses on Street '%s' exceeds maximum of 5", name)
        ); }

    }
    public void sellHouse(int amount) throws negativeHousesException {
        if (houses - amount > 0) {
            houses -= amount;
            owner.addMoney(amount*(housePrice/2));
            rent = priceList.get(houses);
        }
        else { throw new negativeHousesException(String.format("%d is not a valid Value for amount of houses for property: %s", houses-amount, name)); }

    }
    public void landOn(Player p) {
        if (!p.equals(owner) && owner != null) {
            p.removeMoney(rent, true);
            owner.addMoney(rent);
        } else if (owner == null) {
            StreetInfo streetInfo = new StreetInfo(this, parentUI);
            parentUI.drawStreetInfo(streetInfo);
        }
    }

}
