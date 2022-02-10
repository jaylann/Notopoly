package com.company;

abstract class SpecialField {

    protected  Board bp;
    protected  UI parentUI;
    public SpecialField(Board bp, UI parentUI) {
        this.bp = bp;
        this.parentUI = parentUI;
    }

    abstract void landOn(Player p);

}
