package com.company;

import com.company.exceptions.*;

import java.util.Objects;

abstract class Property extends Field{
    protected final int buyPrice;
    protected int sellPrice;
    protected Player owner = null;
    protected int rent;
    protected boolean mortgage = false;
    protected final String name;

    protected boolean isMortgaged() {
        return mortgage;
    }
    protected final int ID;

    public int getID() {
        return ID;
    }

    protected Property(String sName, int price, int id) {
        name = sName;
        buyPrice = price;
        sellPrice = price;
        this.ID = id;
    }


    protected void pay(Player landedPlayer, int payment) throws noOwnerException, propertyMortgagedException {
        if (!mortgage && owner != null) {
            int paidMoney = landedPlayer.removeMoney(payment, true, owner);
            owner.addMoney(paidMoney);
        }
        else if (mortgage) { throw new propertyMortgagedException(String.format("Cannot pay rent to mortgaged property: %s", name)); }
        else { throw new noOwnerException(String.format("Property: %s has no owner to pay rent to", name)); }
    }

    protected void buy(Player buyer) throws alreadyOwnedException {
        if (owner == null) {
            buyer.removeMoney(buyPrice,false, null);
            changeOwner(buyer);
        }
        else { throw new alreadyOwnedException(String.format("Cannot buy already owned property: %s", name)); }
    }

    public Player getOwner() {
        return owner;
    }

    protected void mortgage() throws alreadyMortgagedException, cannotMortgageHousedPropertyException {
        if (!mortgage) {
            mortgage = true;

            owner.addMoney(sellPrice /2);
            System.out.println(owner.getMoney());
        }
        else { throw new alreadyMortgagedException(String.format("Cannot mortgage already mortgaged property: %s", name)); }
    }

    protected void unmortgage() throws notMortgagedException {
        if (mortgage) {
            owner.removeMoney((int) ((sellPrice/2)+(sellPrice+0.1)), false, null);
            mortgage = false;
        }
        else { throw new notMortgagedException(String.format("Cannot unmortgage not mortgaged property: %s", name)); }
    }

    protected boolean changeOwner(Player newOwner) {
        if (owner == null) {
            owner = newOwner;
            return true;
        } else {
            return false;
        }
    }
    protected boolean changeOwner(Player newOwner, boolean override) {
        if (owner == null || override) {
            owner = newOwner;
            return true;
        } else {
            return false;
        }
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public String getName() {
        return name;
    }


}
