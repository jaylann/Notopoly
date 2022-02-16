package com.company;

import java.util.Random;

public class ActionCard {
    protected final Board bp;
    protected final UI parentUI;
    protected final Player targetPlayer;
    protected final Random random = new Random();
    public ActionCard(Board bp, UI parentUI, Player p) {
        this.bp = bp;
        this.parentUI = parentUI;
        this.targetPlayer = p;
    }
}
