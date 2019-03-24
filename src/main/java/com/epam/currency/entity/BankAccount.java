package com.epam.currency.entity;

/**
 * Client's and Stock's bank account
 * It has money in two types and helps to deposit and withdraw easier
 */
public class BankAccount {
    private double BYN;
    private double USD;

    public BankAccount(double BYN, double USD) {
        this.BYN = BYN;
        this.USD = USD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        BankAccount account = (BankAccount) o;

        if (BYN != account.BYN) return false;
        return USD == account.USD;
    }

    @Override
    public int hashCode() {
        return (int) (37*BYN+753*USD+456);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "BYN=" + BYN +
                ", USD=" + USD +
                '}';
    }

    public double getBYN() {
        return BYN;
    }

    public double getUSD() {
        return USD;
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
