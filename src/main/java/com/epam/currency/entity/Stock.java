package com.epam.currency.entity;

import com.epam.currency.entity.observerapi.Observable;
import com.epam.currency.entity.observerapi.Observer;
import com.epam.currency.logic.Deal;
import com.epam.currency.logic.choiseofdecision.Decision;
import com.epam.currency.util.ArgumentValidator;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stock implements Observable {
    private double coefficient;
    private List<Observer> observerList;
    private HashMap<Integer, Deal> dealHashMap;
    private List<Client> BuyCurrencyClientList;
    private List<Client> SellCurrencyClientList;

    private static Stock instance;
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static Lock locker = new ReentrantLock();

    private Stock() {
        if(instance != null){
            throw new RuntimeException("Please use getInstance() method");
        }

        observerList = new ArrayList<>(Collections.emptyList());
        dealHashMap = new HashMap<>();
        coefficient = 2;
        BuyCurrencyClientList = new ArrayList<>(Collections.emptyList());
        SellCurrencyClientList = new ArrayList<>(Collections.emptyList());
    }

    public static Stock getInstance(){
        if(!initialized.get()){
            try {
                locker.lock();
                if (instance == null) {
                    instance = new Stock();
                    initialized.set(true);
                }
            }finally {
                locker.unlock();
            }
        }
        return instance;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public void addObserver(Observer observer) {
        ArgumentValidator.checkForNull(observer);
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        ArgumentValidator.checkForNull(observer);
        observerList.remove(observer);
    }

    @Override
    public void notifyObserver() {
        System.out.println();
        System.out.println("****************( Participants Notify Starts )******************");
        for (Observer o : observerList) {
            o.update(this);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                //logger.error("InterruptedException at notify observer method",e);
            }
        }
        System.out.println("****************( Participants Notify Ends )******************");
        System.out.println();
    }

    public synchronized void sell(Client client) {  //sell USD
        Collections.sort(SellCurrencyClientList);
        SellCurrencyClientList.add(client);

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void buy(Client client) {   //buy USD for Rubles
        Collections.sort(BuyCurrencyClientList);
        BuyCurrencyClientList.add(client);

        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createDeal() {
        int amountOfSellClients = SellCurrencyClientList.size();
        int amountOfBuyClients = BuyCurrencyClientList.size();

        int smallerAmountOfClients;

        if (amountOfSellClients > amountOfBuyClients) {
            smallerAmountOfClients = amountOfBuyClients;
        } else {
            smallerAmountOfClients = amountOfSellClients;
        }

        for (int i=0; i<smallerAmountOfClients; ++i) {

            double USDDealAmount;
            Client buyClient = BuyCurrencyClientList.get(i);
            Client sellClient = SellCurrencyClientList.get(i);

            Decision decision = buyClient.getDecision();
            double buyClientDealMoney = decision.getMoney()/2;

            decision = sellClient.getDecision();
            double sellClientDealMoney = decision.getMoney()/2;

            if (buyClientDealMoney < sellClientDealMoney) {
                USDDealAmount = buyClientDealMoney;
            } else {
                USDDealAmount = sellClientDealMoney;
            }

            Deal deal = new Deal(coefficient, buyClient.getClientAccount(), sellClient.getClientAccount(), USDDealAmount);
            deal.makeDeal();

            if (deal.isFinished()) {
                dealHashMap.put(deal.getId(), deal);

                BuyCurrencyClientList.remove(i);
                SellCurrencyClientList.remove(i);
                i--;
            }
        }
        double differenceOfClientsAmount = (double)amountOfSellClients/amountOfBuyClients;

        Random random = new Random();
        /////
        double changeCoefficientValue = random.nextDouble();  //  (0,7 ; 1)

        if (differenceOfClientsAmount <= 1) {
            coefficient /= changeCoefficientValue;
        } else {
            coefficient *= changeCoefficientValue;
        }

        notifyObserver();
    }
}
