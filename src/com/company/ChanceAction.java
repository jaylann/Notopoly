package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class ChanceAction extends ActionCard{

    public ChanceAction(Board bp, UI parentUI, Player p, SpecialField caller) {
        super(bp, parentUI, p, caller);
        actionList = new ArrayList<Action>(Arrays.asList(new RentAndInterest(), new DrunkAtWork(), new SchoolCost(),
                new GoToJail(), new Renovation(), new BankDividend(), new AdvanceToGo(), new AdvanceToEnd(),
                new SpeedingTicket(), new CrosswordPuzzle(), new StreetImprovements(), new AdvanceToSee(),
                new AdvanceToOper(), new GoBack()));
        Action newAction = getRandomAction();
        showCard(newAction);

    }

    public ChanceAction.Action getRandomAction() {
        return actionList.get(random.nextInt(0, actionList.size()));
    }

    public void showCard(ActionCard.Action action) {
        parentUI.setActionInfo(new ActionInfo(caller,parentUI,action));
    }

    protected class RentAndInterest extends Action {
        private final int reward = 3000;
        public RentAndInterest() {
            super("Miete und Anleihezinsen werden fällig.", "Die Bank zahlt dir 3000€.");
        }
        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class DrunkAtWork extends Action {
        private final int cost = 300;
        public DrunkAtWork() {
            super("Betrunken im Dienst.", "Strafe 300€.");
        }
        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class SchoolCost extends Action {
        private final int cost = 3000;
        public SchoolCost() {
            super("Zahle Schulgeld.", "3000€.");
        }
        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class GoToJail extends Action {
        public GoToJail() {
            super("Gehe in das Gefängnis. \n Begib dich direkt dorthin. \n Gehe nicht über Los.", "Ziehe nicht 4000€ ein.");
        }
        protected void execute() {
            targetPlayer.setPrison(true);
        }
    }
    protected class Renovation extends Action {
        private int houseCost = 500;
        private int hotelCost = 2000;
        public Renovation() {
            super("Lasse alle deine Häuser renovieren", "Zahle für jedes Haus 500€. \n Zahle für jedes Hotel 2000€.");
        }
        protected void execute() {
            for (Street s: targetPlayer.getStreets()){
                if (s.getHouses() == 5) {
                    targetPlayer.payMiddle(hotelCost,bp,true);
                } else {
                    targetPlayer.payMiddle(s.getHouses()*houseCost,bp,true);
                }
            }
        }
    }

    protected class BankDividend extends Action {
        private int reward = 1000;
        public BankDividend() {
            super("Die Bank zahlt dir eine Dividende von", "1000€.");
        }
        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class AdvanceToGo extends Action {
        public AdvanceToGo() {
            super("Rücke vor bis auf", "Los.");
        }
        protected void execute() {
            targetPlayer.setPosition(0);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(0));
        }
    }
    protected class AdvanceToEnd extends Action {
        public AdvanceToEnd() {
            super("Rücke vor bis zur", "Schloßallee.");
        }
        protected void execute() {
            targetPlayer.setPosition(39);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(39));
        }
    }
    protected class SpeedingTicket extends Action {
        private int cost = 300;
        public SpeedingTicket() {
            super("Strafe für zu schnelles Fahren", "300€.");
        }
        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class CrosswordPuzzle extends Action {
        private int reward = 2000;
        public CrosswordPuzzle() {
            super("Du hast in einem Kreuzworträtselwettbewerb gewonnen", "Ziehe 2000€ ein.");
        }
        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class StreetImprovements extends Action {
        private int houseCost = 800;
        private int hotelCost = 2300;
        public StreetImprovements() {
            super("Du wirst zu Straßenausbesserungsarbeiten herangezogen", "Zahle 800€ pro Haus. \n Zahle 2300€ pro Hotel");
        }
        protected void execute() {
            for (Street s: targetPlayer.getStreets()){
                if (s.getHouses() == 5) {
                    targetPlayer.payMiddle(hotelCost,bp,true);
                } else {
                    targetPlayer.payMiddle(s.getHouses()*houseCost,bp,true);
                }
            }
        }
    }
    protected class AdvanceToSee extends Action {
        public AdvanceToSee() {
            super("Rücke vor bis zur Seestraße.", "Wenn du über Los kommst, ziehe 4000€ ein.");
        }
        protected void execute() {
            if(targetPlayer.getPosition() > 11) {
                Go.passBy(targetPlayer);
            }
            targetPlayer.setPosition(11);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(11));
        }
    }
    protected class AdvanceToOper extends Action {
        public AdvanceToOper() {
            super("Rücke vor bis zum Opernplatz.", "Wenn du über Los kommst, ziehe 4000€ ein.");
        }
        protected void execute() {
            if(targetPlayer.getPosition() > 24) {
                Go.passBy(targetPlayer);
            }
            targetPlayer.setPosition(24);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(24));
        }
    }
    protected class GoBack extends Action {
        public GoBack() {
            super("Gehe 3 Felder zurück", "");
        }
        protected void execute() {
            targetPlayer.setPosition(targetPlayer.getPosition()-3);
            targetPlayer.landOn(bp.getPropertyList(targetPlayer.getPosition()));
        }
    }
}
