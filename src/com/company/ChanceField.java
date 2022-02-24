package com.company;

public class ChanceField extends SpecialField{
    public ChanceField(Board bp, UI parentUI, String name) {
        super(bp, parentUI, name);
    }

    @Override
    void landOn(Player p) {
        ActionCard action = new ChanceAction(bp,parentUI,p,this);
    }
}
