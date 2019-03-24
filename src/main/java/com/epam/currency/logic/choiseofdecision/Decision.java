package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;

/**
 * Decision of the client
 * Each implemented class has private field "money". It is money amount that client is ready to pay
 */
public interface Decision {
    void setMoney(Client client);

    double getMoney();
}
