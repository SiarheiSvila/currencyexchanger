package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;
import com.epam.currency.entity.ClientAccount;
import com.epam.currency.entity.Stock;

import java.util.Collections;
import java.util.List;

public class SellCurrency implements Decision {

    private double money;

    @Override
    public synchronized void setMoney(Client client) {
        ClientAccount clientAccount = client.getClientAccount();

        List<Double> coefficients = client.getCoefficients();

        double maxTariff = Collections.max(coefficients);

        double currentUSD = clientAccount.getUSD();
        Stock stock = Stock.getInstance();
        double newBidInUSD = stock.getCoefficient()*currentUSD/maxTariff;



        money = newBidInUSD;
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
