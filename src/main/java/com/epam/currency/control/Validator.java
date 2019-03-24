package com.epam.currency.control;

/**
 * Validates a String of data
 */
public class Validator {

    private final String ID_AND_TWO_MONEY_TYPES = "[1-9]\\d*(\\s(0|([1-9][0-9]*))(\\.[0-9]+)?){2}";

    public boolean isDataCorrect(String data) {

        return data.matches(ID_AND_TWO_MONEY_TYPES);
    }
}
