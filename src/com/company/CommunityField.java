package com.company;

public class CommunityField extends SpecialField{
    public CommunityField(Board bp, UI parentUI, String name) {
        super(bp, parentUI, name);
    }

    @Override
    void landOn(Player p) {
        ActionCard action = new CommunityAction(bp,parentUI,p);
    }
}
