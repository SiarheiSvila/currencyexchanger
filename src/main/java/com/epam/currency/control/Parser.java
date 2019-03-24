package com.epam.currency.control;

import com.epam.currency.entity.Client;

/**
 * Parses a String of data to Client Object
 */
public class Parser {

    public Client createClient(String data) {

        String[] idAndMoney = data.split("\\s");

        int id = Integer.parseInt(idAndMoney[0]);
        double BYN = Double.parseDouble(idAndMoney[1]);
        double USD = Double.parseDouble(idAndMoney[2]);

        return new Client(id, BYN, USD);
    }
}
