package com.company;

public class JailingField extends SpecialField{
    public JailingField(Board bp, UI parentUI) {
        super(bp, parentUI);
    }

    @Override
    void landOn(Player p) {
        p.setPosition(10);
        p.setPrison(true);
    }
}
