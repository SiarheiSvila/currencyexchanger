package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;
import com.epam.currency.entity.BankAccount;
import com.epam.currency.entity.Stock;

import java.util.Collections;
import java.util.List;

/**
 * Sells some USD to get maximum profit
 */
public class SellCurrency implements Decision {
    private double money;

    @Override
    public void setMoney(Client client) {
        BankAccount clientAccount = client.getBankAccount();

        List<Double> coefficients = client.getCoefficients();

        double maxTariff = Collections.max(coefficients);

        double currentUSD = clientAccount.getUSD();
        Stock stock = Stock.getInstance();
        double newBidInUSD = stock.getCoefficient()*currentUSD/maxTariff;

        money = newBidInUSD;
    }

    @Override
    public double getMoney() {
        return money;
    }
}
