package com.company;

import com.company.exceptions.noOwnerException;
import com.company.exceptions.propertyMortgagedException;

import java.util.ArrayList;

public class TrainStation extends Property{


    private final int rent;
    private int multiplier;
    private final UI parentUI;
    
    public int getRent() {
        return rent;
    }
    public int getCurrentRent() {
        return rent*getMultiplier();
    }

    protected TrainStation(String sName, int price, int rent, UI parentUI, int index) {
        super(sName, price, index);
        this.rent = rent;
        this.parentUI = parentUI;
    }

    private int getMultiplier() {
        multiplier = (int) Math.pow(2,getOwnedTrainStations().size());
        return multiplier;
    }
    public ArrayList<TrainStation> getOwnedTrainStations() {
        ArrayList<TrainStation> blockList = new ArrayList<>();
        for(Property prop: owner.getProperties()) {
            if (prop.getClass() == TrainStation.class && !prop.isMortgaged()) {
                blockList.add((TrainStation) prop);
            }
        }
        return blockList;
    }
    @Override
    void landOn(Player p) {
        if (!p.equals(owner) && owner != null) {

            int payment = rent*getMultiplier();

            try {
                pay(p, payment);
            } catch (noOwnerException e) {
                e.printStackTrace();
            } catch (propertyMortgagedException e) {
                parentUI.drawMortgageWarning();
            }
        } else if (owner == null) {
            TrainInfo trainInfo = new TrainInfo(this, parentUI);
            parentUI.drawInfo(trainInfo);
        }
    }
}
