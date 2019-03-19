package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;
import com.epam.currency.entity.ClientAccount;
import com.epam.currency.entity.Stock;

public class BuyCurrency implements Decision {

    private double money;

    @Override
    public synchronized void setMoney(Client client) {
        ClientAccount clientAccount = client.getClientAccount();
        double currentUSD = clientAccount.getUSD();
        double newBidInUSD = 0.3*currentUSD;

        Stock stock = Stock.getInstance();

        money = newBidInUSD/stock.getCoefficient();  //Что платит в рублях чтобы получить выгоду в долларах в 30%
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public synchronized double getMoney() {
        return money;
    }
}
