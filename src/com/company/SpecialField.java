package com.company;

abstract class SpecialField extends Field {

    protected  Board bp;
    protected  UI parentUI;
    protected String name;
    public SpecialField(Board bp, UI parentUI, String name) {
        this.bp = bp;
        this.parentUI = parentUI;
        this.name = name;
    }

    public String getName() {return this.name;}

}
