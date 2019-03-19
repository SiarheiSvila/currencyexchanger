package com.epam.currency.entity;

import com.epam.currency.entity.observerapi.Observer;
import com.epam.currency.logic.choiseofdecision.BuyCurrency;
import com.epam.currency.logic.choiseofdecision.Decision;
import com.epam.currency.logic.choiseofdecision.DoNothing;
import com.epam.currency.logic.choiseofdecision.SellCurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Observer, Runnable, Comparable<Client>{

    private ClientAccount clientAccount;
    private Lock lock = new ReentrantLock();
    private List<Double> coefficients;
    private Decision decision;

    public Client(double BYN, double USD, Decision decision) {
        clientAccount = new ClientAccount(BYN, USD);
        coefficients = new ArrayList<>(Collections.emptyList());
        this.decision = decision;
    }

    @Override
    public void update(Stock stock) {
        System.out.println(Thread.currentThread().getName() + " is notified");
        double currentCoefficient = stock.getCoefficient();
        coefficients.add(currentCoefficient);
    }

    @Override
    public int compareTo(Client o) {
        Decision parameterDecision = o.getDecision();
        Double parameterDecisionMoneyAmount = parameterDecision.getMoney();
        double thisDecisionMoneyAmount = decision.getMoney();

        return parameterDecisionMoneyAmount.compareTo(thisDecisionMoneyAmount);
    }

    @Override
    public void run() {

        Stock stock = Stock.getInstance();

        for (int i=0; i<5; i++) {
            if (i != 0) {
                decision = makeDecision();
            }

            double moneyAmount = decision.getMoney();

            if (decision instanceof BuyCurrency) {
                stock.buy(this);
            } else if (decision instanceof SellCurrency) {
                stock.sell(this);
            }

        }
    }

    public Decision getDecision() {
        return decision;
    }

    public ClientAccount getClientAccount() {
        return clientAccount;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public Decision makeDecision() {
        lock.lock();

        Stock stock = Stock.getInstance();

        Decision decision;
        try {
            ///
            System.out.println("  makeDecision " + Thread.currentThread().getName());

//            if (coefficients.isEmpty()) {  // If it is the first lap of CurrencyExchange
//                Random random = new Random();
//                double preferredTariff = 8 * random.nextDouble();
//
//                if (preferredTariff >= stock.getCoefficient()) {  //коэффициент уменьшился
//                    decision = new BuyCurrency();                  //buy USD for Rubles
//
//                } else {                                    //коэффициент вырос
//                    decision = new SellCurrency();                  //sell USD
//                }
//
//            } else {


                int amountOfActions = coefficients.size();
                double previousTariff = coefficients.get(amountOfActions - 2);

                double tariffDifference = stock.getCoefficient() / previousTariff;

                if (tariffDifference >= 1.3) {
                    decision = new BuyCurrency();
                    decision.setMoney(this);

                } else if (tariffDifference < 1) {
                    decision = new SellCurrency();
                    decision.setMoney(this);

                } else {
                    decision = new DoNothing();

                }

//            }

            double currentTariff = stock.getCoefficient();
            coefficients.add(currentTariff);


        } finally {
            lock.unlock();
        }
        return decision;
    }

    public void getClientInfo() {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + "   Money: " + clientAccount.getUSD() + "$   " + clientAccount.getBYN() + "P");
    }


}

