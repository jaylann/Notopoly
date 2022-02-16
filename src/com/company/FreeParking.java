package com.company;

public class FreeParking extends SpecialField{
    public FreeParking(Board bp, UI parentUI, String name) {
        super(bp, parentUI, name);
    }

    @Override
    void landOn(Player p) {
        p.addMoney(bp.getMiddleMoney());
        bp.setMiddleMoney(0);
    }
}
