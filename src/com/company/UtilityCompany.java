package com.company;

import com.company.exceptions.noOwnerException;
import com.company.exceptions.propertyMortgagedException;

import java.util.ArrayList;

public class UtilityCompany extends Property{

    private int multiplier;
    private final UI parentUI;

    protected UtilityCompany(String sName, int price, UI parentUI) {
        super(sName, price);
        this.parentUI = parentUI;
    }

    private int getMultiplier(){
        if (getOwnedUtilityCompanies().size() == 1 ) {
            multiplier = 4;
        } else if (getOwnedUtilityCompanies().size() == 2) {
            multiplier = 10;
        }
        return multiplier;
    }

    @Override
    void landOn(Player p) {
        if (!p.equals(owner) && owner != null) {

            int payment = multiplier*p.getRecentRoll();

            try {
                pay(p, payment);
            } catch (noOwnerException e) {
                e.printStackTrace();
            } catch (propertyMortgagedException e) {
                parentUI.drawMortgageWarning();
            }
        } else if (owner == null) {
            UtilityInfo utilityInfo = new UtilityInfo(this, parentUI);
            parentUI.drawInfo(utilityInfo);
        }
    }

    private ArrayList<UtilityCompany> getOwnedUtilityCompanies() {
        ArrayList<UtilityCompany> blockList = new ArrayList<>();
        for(Property prop: owner.getProperties()) {
            if (prop.getClass() == UtilityCompany.class && !prop.isMortgaged()) {
                blockList.add((UtilityCompany) prop);
            }
        }
        return blockList;
    }
}
