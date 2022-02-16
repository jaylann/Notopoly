package com.company;

import org.jetbrains.annotations.NotNull;

public class TaxField extends SpecialField{
    private final int payment;
    public TaxField(Board bp, UI parentUI, String name, int payment) {
        super(bp, parentUI, name);
        this.payment = payment;
    }

    @Override
    void landOn(@NotNull Player p) {
        p.payMiddle(payment, bp, true);
        System.out.println(bp.getMiddleMoney());
    }
}
