package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;

public class DoNothing implements Decision{

    private double money;

    @Override
    public synchronized void setMoney(Client client) {
        money = 0;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public synchronized double getMoney() {
        return 0;
    }
}
