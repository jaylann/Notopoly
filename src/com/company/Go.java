package com.company;

public class Go extends SpecialField {
    public Go(Board bp, UI parentUI) {
        super(bp, parentUI);
    }

    @Override
    void landOn(Player p) {
        p.addMoney(8000);
    }
    void passBy(Player p) {
        p.addMoney(4000);
    }
}
