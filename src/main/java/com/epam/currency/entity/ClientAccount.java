package com.epam.currency.entity;

public class ClientAccount {
    private double BYN;
    private double USD;

    public ClientAccount(double BYN, double USD) {
        this.BYN = BYN;
        this.USD = USD;
    }

    public double getBYN() {
        return BYN;
    }

    public void setBYN(double BYN) {
        this.BYN = BYN;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public void addBYN (double money) {
        this.BYN += money;
    }

    public void subtractBYN (double money) {
        this.BYN -= money;
    }

    public void addUSD (double money) {
        this.USD += money;
    }

    public void subtractUSD (double money) {
        this.USD -= money;
    }
}
