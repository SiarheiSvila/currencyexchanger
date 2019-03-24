package com.epam.currency.entity;

import com.epam.currency.entity.observerapi.Observable;
import com.epam.currency.entity.observerapi.Observer;
import com.epam.currency.logic.choiseofdecision.Decision;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Stock has coefficient, it's own bank account and a List of clients to notify them if coefficient has changed
 * When one client enters the stock, other are blocked
 */
public class Stock implements Observable, Runnable{
    private double coefficient;
    private List<Observer> observerList;
    private BankAccount stockAccount;

    private static Stock instance;
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static Lock lock = new ReentrantLock();
    private static final Logger LOGGER = LogManager.getLogger(Stock.class);

    private Stock() {
        if(instance != null){
            throw new RuntimeException("Please use getInstance() method");
        }

        observerList = new ArrayList<>(Collections.emptyList());
        coefficient = 2;

        stockAccount = new BankAccount(10000000,10000000);
    }

    public static Stock getInstance(){
        if(!initialized.get()){
            try {
                lock.lock();
                if (instance == null) {
                    instance = new Stock();
                    initialized.set(true);
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public double getCoefficient() {
        return coefficient;
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            LOGGER.warn("Error, observer can't be null");
            throw new IllegalArgumentException("Error in addObserverMethod");
        }
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observer == null) {
            LOGGER.warn("Error, observer can't be null");
            throw new IllegalArgumentException("Error in removeObserverMethod");
        }
        observerList.remove(observer);
    }

    @Override
    public void notifyObserver() {
        lock.lock();

        try {
            LOGGER.info("     ####    new coefficient: " + coefficient + "    ####\n");
            LOGGER.info("****************( Participants Notify Starts )******************");
            for (Observer o : observerList) {
                o.update(this);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    //logger.error("InterruptedException at notify observer method",e);
                }
            }
            LOGGER.info("****************( Participants Notify Ends )******************\n");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        LOGGER.info("   " + Thread.currentThread().getName() + " begin running\n");
        notifyObserver();
        LOGGER.info("   " + Thread.currentThread().getName() + " end running\n");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;

        if (stock.coefficient != coefficient) return false;
        if (!stockAccount.equals(stock.stockAccount)) return false;

        return observerList.equals(stock.observerList);
    }

    @Override
    public int hashCode() {
        return (int) (53*coefficient + 14*observerList.hashCode() + 37*stockAccount.hashCode());
    }

    @Override
    public String toString() {
        return "Stock{" +
                "coefficient=" + coefficient +
                ", observerList=" + observerList +
                ", stockAccount=" + stockAccount +
                '}';
    }

    public void sell(Client client) {
        if (client == null) {
            LOGGER.warn("Error, client can't be null");
            throw new IllegalArgumentException("Error in sellMethod");
        }

        Decision clientDecision = client.getDecision();
        double dealMoneyAmountUSD = clientDecision.getMoney();
        double dealMoneyAmountBYN = dealMoneyAmountUSD*coefficient;

        BankAccount clientAccount = client.getBankAccount();

        if (clientAccount.getUSD() < dealMoneyAmountUSD) {
            LOGGER.warn("Sorry, client doesn't have enough money. A deal can't be made");
        } else {
            clientAccount.subtractUSD(dealMoneyAmountUSD);
            stockAccount.addUSD(dealMoneyAmountUSD);
            clientAccount.addBYN(dealMoneyAmountBYN);
            stockAccount.subtractBYN(dealMoneyAmountUSD);
        }

        makeCoefficientSmaller();
    }

    public void buy(Client client) {
        if (client == null) {
            LOGGER.warn("Error, client can't be null");
            throw new IllegalArgumentException("Error in buyMethod");
        }

        Decision clientDecision = client.getDecision();
        double dealMoneyAmountBYN = clientDecision.getMoney();
        double dealMoneyAmountUSD = dealMoneyAmountBYN/coefficient;

        BankAccount clientAccount = client.getBankAccount();

        if (clientAccount.getBYN() < dealMoneyAmountBYN) {
            LOGGER.warn("Sorry, client doesn't have enough money. A deal can't be made");
        } else {
            clientAccount.subtractBYN(dealMoneyAmountBYN);
            stockAccount.addBYN(dealMoneyAmountBYN);
            clientAccount.addUSD(dealMoneyAmountUSD);
            stockAccount.subtractUSD(dealMoneyAmountUSD);
        }

        makeCoefficientBigger();
    }

    private void makeCoefficientSmaller() {
        coefficient -= 0.2;

        notifyObserver();
    }

    private void makeCoefficientBigger() {
        coefficient += 0.2;

        notifyObserver();
    }
}
