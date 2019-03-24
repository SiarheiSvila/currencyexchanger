package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;

/**
 * Client doesn't sell and doesn't buy
 */
public class DoNothing implements Decision{
    private double money;

    @Override
    public void setMoney(Client client) {
        money = 0;
    }

    @Override
    public double getMoney() {
        return 0;
    }
}
