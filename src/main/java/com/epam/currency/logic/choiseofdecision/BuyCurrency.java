package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;
import com.epam.currency.entity.BankAccount;
import com.epam.currency.entity.Stock;

/**
 * Pays some BYN to get 30% profit in dollars
 */
public class BuyCurrency implements Decision {
    private double money;

    @Override
    public void setMoney(Client client) {
        BankAccount clientAccount = client.getBankAccount();
        double currentUSD = clientAccount.getUSD();
        double newBidInUSD = 0.3*currentUSD;

        Stock stock = Stock.getInstance();

        money = newBidInUSD/stock.getCoefficient();  //Что платит в рублях чтобы получить выгоду в долларах в 30%
    }

    @Override
    public double getMoney() {
        return money;
    }
}
