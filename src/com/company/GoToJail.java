package com.company;

public class GoToJail extends SpecialField{
    public GoToJail(Board bp, UI parentUI, String name) {
        super(bp, parentUI, name);
    }

    @Override
    void landOn(Player p) {
        p.setPrison(true);
    }
}
