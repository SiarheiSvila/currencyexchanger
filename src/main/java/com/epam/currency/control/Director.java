package com.epam.currency.control;

import com.epam.currency.entity.Client;
import com.epam.currency.exceptions.FileReaderException;
import com.epam.currency.scanner.FileReader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *Manages operations on reading data from file, validating it and parsing to object
 */
public class Director {
    final private static Logger LOGGER = Logger.getLogger(Director.class.getName());

    private final FileReader fileReader;
    private final Validator validator;
    private final Parser parser;


    public Director(FileReader fileReader, Validator validator, Parser parser) {
        this.fileReader = fileReader;
        this.validator = validator;
        this.parser = parser;
    }

    public List<Client> createClientList(String path) {

        List<Client> clientList = new ArrayList<>();

        List<String> data;

        try {
            data = fileReader.read(path);
        } catch (FileReaderException e) {
            throw new IllegalArgumentException();
        }

        for (String line : data) {
            //check whole line in validator
            //creates an object and adds it to List
            if (validator.isDataCorrect(line)) {
                Client client = parser.createClient(line);

                if (client == null) {
                    continue;
                }

                clientList.add(client);
            }

        }

        if (clientList.size() == 0) {
            LOGGER.error("Error in creating an client: ");
            throw new IllegalArgumentException("Error. No suitable data found");
        }

        return clientList;

    }
}
