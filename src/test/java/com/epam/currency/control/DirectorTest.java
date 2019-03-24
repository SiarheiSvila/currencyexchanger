package com.epam.currency.control;

import com.epam.currency.entity.Client;
import com.epam.currency.exceptions.FileReaderException;
import com.epam.currency.scanner.FileReader;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DirectorTest {
    private final List<Client> EXPECTED_LIST_FOR_TEST_1 = Arrays.asList(new Client(1, 400, 60));
    private final List<Client> EXPECTED_LIST_FOR_TEST_2 = Arrays.asList(new Client(1, 400, 60),
                                                                    new Client(2, 590, 5));

    @Test
    public void shouldDirectorCreateClientListOneGoodClientSupplied() {
        //given
        String path = "./src/test/resources/shouldDirectorCreateClientListOneGoodClientSupplied.txt";

        FileReader fileReader = Mockito.mock(FileReader.class);
        when(fileReader.read(anyString())).thenReturn(Arrays.asList("1 400 60"));

        Validator validator = Mockito.mock(Validator.class);
        when(validator.isDataCorrect(anyString())).thenReturn(true);

        Parser parser = Mockito.mock(Parser.class);
        when(parser.createClient(anyString())).thenReturn(EXPECTED_LIST_FOR_TEST_1.get(0));



        Director director = new Director(fileReader, validator, parser);

        //when
        List<Client> result = director.createClientList(path);

        //then
        Assert.assertEquals(EXPECTED_LIST_FOR_TEST_1, result);

        verify(fileReader).read(anyString());
        verify(validator).isDataCorrect(anyString());
        verify(parser).createClient(anyString());

        verifyNoMoreInteractions(fileReader, validator, parser);
    }

    @Test
    public void shouldDirectorCreateClientListTwoGoodClientsSupplied() {
        //given
        String path = "./src/test/resources/shouldDirectorCreateClientListTwoGoodClientsSupplied.txt";

        FileReader fileReader = Mockito.mock(FileReader.class);
        when(fileReader.read(anyString())).thenReturn(Arrays.asList("1 400 60", "2 590 5"));

        Validator validator = Mockito.mock(Validator.class);
        when(validator.isDataCorrect(anyString())).thenReturn(true);

        Parser parser = Mockito.mock(Parser.class);
        when(parser.createClient("1 400 60")).thenReturn(EXPECTED_LIST_FOR_TEST_2.get(0));
        when(parser.createClient("2 590 5")).thenReturn(EXPECTED_LIST_FOR_TEST_2.get(1));


        Director director = new Director(fileReader, validator, parser);

        //when
        List<Client> result = director.createClientList(path);

        //then
        Assert.assertEquals(EXPECTED_LIST_FOR_TEST_2, result);

        verify(fileReader).read(anyString());
        verify(validator).isDataCorrect("1 400 60");
        verify(validator).isDataCorrect("2 590 5");
        verify(parser).createClient("1 400 60");
        verify(parser).createClient("2 590 5");

        verifyNoMoreInteractions(fileReader, validator, parser);
    }
}
