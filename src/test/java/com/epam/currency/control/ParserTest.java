package com.epam.currency.control;

import com.epam.currency.entity.BankAccount;
import com.epam.currency.entity.Client;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    private Parser parser = new Parser();

    @Test
    public void shouldParserCreateClientIntegerVariablesSupplied(){
        //given

        //when
        Client result = parser.createClient("1 3 5");

        //then
        Assert.assertEquals(result.getBankAccount(), new BankAccount(3, 5));
    }

    @Test
    public void shouldParserCreateClientSomeDoubleVariablesSupplied(){
        //given

        //when
        Client result = parser.createClient("6 100.35 67.1");

        //then
        Assert.assertEquals(result.getBankAccount(), new BankAccount(100.35, 67.1));
    }
}
