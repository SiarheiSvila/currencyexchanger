package com.epam.currency.logic.choiseofdecision;

import com.epam.currency.entity.Client;

public interface Decision {
    void setMoney(Client client);
    void setMoney(double money);

    double getMoney();
}
