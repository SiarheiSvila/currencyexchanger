package com.epam.currency.util;

public class ArgumentValidator {
    public static void checkForNegativity(double number){
        if(number < 0){
            throw new IllegalArgumentException("Not allow for a negative number");
        }
    }

    public static void checkForNull(Object object){
        if(object == null){
            throw new IllegalArgumentException("Not allow for null object");
        }
    }

    public static void checkForNullOrEmptyString(String string){
        if(string == null || string.isEmpty()){
            throw new IllegalArgumentException("Not allow for null string value or empty value");
        }
    }
}
