package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class ActionCard {
    protected final Board bp;
    protected final UI parentUI;
    protected final Player targetPlayer;

    protected ArrayList<Action> actionList;
    protected final Random random = new Random();
    protected final SpecialField caller;
    public ActionCard(Board bp, UI parentUI, Player p, SpecialField caller) {
        this.bp = bp;
        this.parentUI = parentUI;
        this.targetPlayer = p;
        this.caller = caller;

    }

    public Player getPlayer() {
        return targetPlayer;
    }

    public UI getUI() {
        return parentUI;
    }

    public Board getBoard() {
        return bp;
    }

    protected abstract class Action {
        protected final String title;
        protected final String infoText;
        protected boolean choice = false;

        public boolean hasChoice() {
            return choice;
        }

        public Action(String action,String infoText) {
            this.title = action;
            this.infoText = infoText;
        }

        protected ActionListener acceptListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute();
                if (!targetPlayer.isDoublets()) {
                    parentUI.setNextTurnAvailable(true);
                }
                parentUI.disableInfo();
            }
        };

        public ActionListener getAcceptListener() {return acceptListener;}

        abstract void execute();
        protected String getTitle() {
            return this.title;
        }
        protected String getInfoText() {
            return this.infoText;
        }


        public ActionListener getChoiceListener() { return null; }
    }
}
