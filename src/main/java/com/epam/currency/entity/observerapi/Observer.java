package com.epam.currency.entity.observerapi;

import com.epam.currency.entity.Stock;

public interface Observer {
    void update(Stock stock);
}
