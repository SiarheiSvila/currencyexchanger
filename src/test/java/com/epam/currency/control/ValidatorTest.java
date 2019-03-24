package com.epam.currency.control;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorTest {
    private Validator validator = new Validator();

    @Test
    public void shouldValidatorIsDataCorrectIntegerSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("1 4.8 5");

        //then
        Assert.assertTrue(result);
    }

    @Test
    public void shouldValidatorIsDataCorrectSomeNegativeVariablesSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("45 -0.1 920");

        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectZeroIdSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("0 5 6");

        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectNoFractionSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("1 3. 6");

        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectNoIdSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("2 5");


        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectTwoVariablesSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("1 2");


        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectOneVariableSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("1");


        //then
        Assert.assertTrue(!result);
    }

    @Test
    public void shouldValidatorIsDataCorrectNoVariablesSupplied(){
        //given

        //when
        boolean result = validator.isDataCorrect("");


        //then
        Assert.assertTrue(!result);
    }
}
