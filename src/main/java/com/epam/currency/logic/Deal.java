package com.epam.currency.logic;

import com.epam.currency.entity.ClientAccount;
import com.epam.currency.entity.Stock;

import java.util.Random;

public class Deal {
    private double coefficient;
    private ClientAccount buyClient;
    private ClientAccount sellClient;
    private Integer id;
    private double dealAmount;

    private boolean finished;

    public Deal(double coefficient, ClientAccount buyClient, ClientAccount sellClient, double USDMoneyAmount) {
        this.coefficient = coefficient;
        this.buyClient = buyClient;
        this.sellClient = sellClient;

        setId();

        finished = false;
        dealAmount = USDMoneyAmount;
    }

    public void setId() {
        Random random = new Random();
        id = random.nextInt();
    }

    public Integer getId() {
        return id;
    }

    public void makeDeal() {
        buyClient.addUSD(dealAmount);
        buyClient.subtractBYN(dealAmount*coefficient);

        sellClient.subtractUSD(dealAmount);
        sellClient.addBYN(dealAmount*coefficient);

        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
}
