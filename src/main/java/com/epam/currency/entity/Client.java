package com.epam.currency.entity;

import com.epam.currency.entity.observerapi.Observer;
import com.epam.currency.logic.Controller;
import com.epam.currency.logic.choiseofdecision.BuyCurrency;
import com.epam.currency.logic.choiseofdecision.Decision;
import com.epam.currency.logic.choiseofdecision.DoNothing;
import com.epam.currency.logic.choiseofdecision.SellCurrency;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Client of the Stock
 * He has id, bankAccount, all coefficients that had ever appeared on the stock and decision
 * Implements observer to add new coefficient to list, when it changes
 * When object is created, first element in coefficients is preferred coefficient
 */
public class Client implements Observer, Runnable{

    private final Integer id;
    private BankAccount clientAccount;
    private List<Double> coefficients;
    private Decision decision;

    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    public Client(Integer id, double BYN, double USD, Decision decision) {
        this.id = id;
        clientAccount = new BankAccount(BYN, USD);
        coefficients = new ArrayList<>(Arrays.asList(preferredCoefficientCount()));
        this.decision = decision;
    }

    public Client(Integer id, double BYN, double USD) {
        this.id = id;
        clientAccount = new BankAccount(BYN, USD);
        coefficients = new ArrayList<>(Arrays.asList(preferredCoefficientCount()));
        decision = new DoNothing();
    }

    public Client() {
        id = 0;
    }

    private double preferredCoefficientCount() {
        return  2*(Math.random() * 0.3) + 1;
    }

    @Override
    public void update(Stock stock) {
        LOGGER.info("client" + id + " is notified");
        double currentCoefficient = stock.getCoefficient();
        coefficients.add(currentCoefficient);
    }

    @Override
    public void run() {
        Controller controller = Controller.getInstance();
        Stock stock = Stock.getInstance();

        while (decision instanceof DoNothing) {

            if (controller.isFree()) {

                controller.setOccupied();

                LOGGER.info("   ---- " + Thread.currentThread().getName() + " entered the stock ----");

                LOGGER.info(Thread.currentThread().getName() + " coefficients: " + coefficients);

                makeDecision();

                if (decision instanceof BuyCurrency) {
                    LOGGER.info(Thread.currentThread().getName() + " buys USD for " + decision.getMoney() + " BYN\n");
                    stock.buy(this);
                } else if (decision instanceof SellCurrency) {
                    LOGGER.info(Thread.currentThread().getName() + " sells " + decision.getMoney() + " USD\n");
                    stock.sell(this);
                }

                controller.setFree();
                stock.removeObserver(this);
                LOGGER.info("   " + Thread.currentThread().getName() + " exits the stock\n");
            } else {

                LOGGER.debug("              $$$$   " + Thread.currentThread().getName() + " tried to enter the stock, but somebody is already there   $$$$");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Client client = (Client) o;

        if (id != client.id) return false;
        if (!clientAccount.equals(client.clientAccount)) return false;
        return coefficients.equals(client.coefficients);
    }

    @Override
    public int hashCode() {
        return 34*coefficients.hashCode() + 21*decision.hashCode() + 76*id + 4*clientAccount.hashCode();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientAccount=" + clientAccount +
                ", coefficients=" + coefficients +
                ", decision=" + decision +
                '}';
    }

    public Decision getDecision() {
        return decision;
    }

    public BankAccount getBankAccount() {
        return clientAccount;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public Integer getId() {
        return id;
    }

    public void makeDecision() {
        Stock stock = Stock.getInstance();
        int amountOfActions = coefficients.size();
        double previousCoefficient = coefficients.get(amountOfActions - 2);

        double coefficientDifference = stock.getCoefficient() / previousCoefficient;

        if (coefficientDifference >= 1.3) {
            decision = new BuyCurrency();
            decision.setMoney(this);
         } else {
            decision = new SellCurrency();
            decision.setMoney(this);
        }
    }
}

