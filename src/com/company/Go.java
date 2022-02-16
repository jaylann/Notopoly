package com.company;

public class Go extends SpecialField {
    public Go(Board bp, UI parentUI, String name) {
        super(bp, parentUI, name);
    }

    @Override
    void landOn(Player p) {
        p.addMoney(8000);
    }
    static void passBy(Player p) {
        p.addMoney(4000);
    }
}
