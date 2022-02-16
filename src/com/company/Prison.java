package com.company;

public class Prison extends SpecialField{
    public Prison(Board bp, UI parentUI, String name, int price) {
        super(bp, parentUI, name);
        this.price = price;
    }

    private final int price;

    @Override
    void landOn(Player p) {

    }

    public int getPrice() {
        return price;
    }
}
