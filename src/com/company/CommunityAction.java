package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;


public class CommunityAction extends ActionCard{

    public CommunityAction(Board bp, UI parentUI, Player p, SpecialField caller) {
        super(bp, parentUI, p, caller);
        actionList = new ArrayList<>(Arrays.asList(new AdvanceToGo(), new Birthday(), new StockPayout(),
                new IncomeTaxReward(), new PrisonFree(), new Insurance(), new BackToStart(), new PayOrChance(),
                new BeautyContest(), new Hospital(), new Doctor(), new GarageSale(), new Pension(), new GoToPrison(),
                new Inheritance(), new BankError()));
        Action newAction = getRandomAction();
        showCard(newAction);

    }

    public CommunityAction.Action getRandomAction() {
        return actionList.get(random.nextInt(0, actionList.size()));
    }

    public void showCard(Action action) {
        parentUI.setActionInfo(new ActionInfo(caller,parentUI,action));
    }


    protected class AdvanceToGo extends Action {
        public AdvanceToGo() {
            super("Rücken sie vor auf", "Los.");
        }
        protected void execute() {
            targetPlayer.setPosition(0);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(0));
        }
    }

    protected class Birthday extends Action {
        private final int cost = 200;
        public Birthday() {
            super("Es ist dein Geburtstag.", "Ziehe von jedem Spieler 200€ ein.");
        }
        protected void execute() {
            int payedMoney;
            for (Player p: bp.getPlayerList()) {
                if (!p.equals(targetPlayer)) {
                    System.out.println(String.format("REMOVED %d€ from PLAYER: %s", cost,p.getName()));
                    payedMoney = p.removeMoney(cost,true);
                    targetPlayer.addMoney(payedMoney);
                }
            }
        }
    }
    protected class StockPayout extends Action {
        private final int reward = 500;
        public StockPayout() {
            super("Du erhälst auf Vorzugs-Aktien 7% Dividende", "500€.");
        }
        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class IncomeTaxReward extends Action {
        private final int reward = 400;
        public IncomeTaxReward() {
            super("Einkommensteuer-Rückzahlung.", "Ziehe 400€ ein.");
        }
        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class PrisonFree extends Action {
        public PrisonFree() {
            super("Du kommst aus dem Gefängnis frei.", "Diese Karte muss behalten werden, bis sie gebraucht oder verkauft wird.");
        }
        protected void execute() {
            targetPlayer.addPrisonFree();
        }
    }
    protected class Insurance extends Action {
        private final int cost = 1000;
        public Insurance() {
            super("Zahle deine Versicherungssumme.", "1000€.");
        }
        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class BackToStart extends Action {
        public BackToStart() {
            super("Gehe zurück bis zu der.", "Badstraße.");
        }
        protected void execute() {
            targetPlayer.setPosition(3);
            parentUI.disableInfo();
            targetPlayer.landOn(bp.getPropertyList(3));
        }
    }
    protected class PayOrChance extends Action {
        private final int cost = 200;
        private boolean pay = true;
        public PayOrChance() {
            super("Du hast die wahl zwischen", "Zahle 200€ oder nimm eine Ereigniskarte.");
        }
        public void setPay(boolean pay) {
            this.pay = pay;
        }

        private final ActionListener choiceListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pay = false;
                execute();
                parentUI.disableInfo();
            }
        };

        public ActionListener getChoiceListener() {return choiceListener;}

        protected void execute() {
            if (pay) {
                targetPlayer.payMiddle(cost,bp,true);
            } else {
                //TODO: Implement Drawing of Chance Card
            }
        }
    }
    protected class BeautyContest extends Action {
        private final int reward = 200;
        public BeautyContest() {
            super("Du hast den II. Preis in einer Schönheitskonkurrenz gewonnen.", "Ziehe 200€ ein.");
        }

        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class Hospital extends Action {
        private final int cost = 2000;
        public Hospital() {
            super("Zahle an das Krankenhaus", "2000€.");
        }

        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class Doctor extends Action {
        private final int cost = 100;
        public Doctor() {
            super("Arzt-Kosten", "100€.");
        }

        protected void execute() {
            targetPlayer.payMiddle(cost,bp,true);
        }
    }
    protected class GarageSale extends Action {
        private final int reward = 100;
        public GarageSale() {
            super("Aus Lagerverkäufen erhälst du", "100€.");
        }

        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class Pension extends Action {
        private final int reward = 2000;
        public Pension() {
            super("Die Jahresrente wird fällig.", "Ziehe 2000€ ein.");
        }

        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class GoToPrison extends Action {
        public GoToPrison() {
            super("Gehe in das Gefängnis. \n Begib dich direkt dorthin. \n Gehe nicht über Los.", "Ziehe nicht 4000€ ein.");
        }

        protected void execute() {
            targetPlayer.setPrison(true);
        }
    }
    protected class Inheritance extends Action {
        private final int reward = 2000;
        public Inheritance() {
            super("Du erbst", "2000€.");
        }

        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
    protected class BankError extends Action {
        private final int reward = 4000;
        public BankError() {
            super("Bank-Irrtum zu deinen Gunsten", "Ziehe 4000€ ein.");
        }

        protected void execute() {
            targetPlayer.addMoney(reward);
        }
    }
}
