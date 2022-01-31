package com.company;

import com.company.exceptions.alreadyMortgagedException;
import com.company.exceptions.cannotMortgageHousedPropertyException;
import com.company.exceptions.maxHousesPerPropertyReachedException;
import com.company.exceptions.negativeHousesException;

import javax.lang.model.element.UnknownElementException;
import java.util.ArrayList;

public class Street extends Property {
    private int houses = 0;
    private final int housePrice;
    private final ArrayList<Integer> priceList;
    private boolean monopoly = false;

    public Street(String sName, int price, ArrayList<Integer> pay, int hPrice) {
        super(sName,price);
        priceList = pay;
        housePrice = hPrice;
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
            owner.removeMoney(amount*housePrice);
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

}
