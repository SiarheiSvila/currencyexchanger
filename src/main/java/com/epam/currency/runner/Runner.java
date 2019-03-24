package com.epam.currency.runner;

import com.epam.currency.control.Director;
import com.epam.currency.control.Parser;
import com.epam.currency.control.Validator;
import com.epam.currency.entity.Client;
import com.epam.currency.entity.Stock;
import com.epam.currency.scanner.FileReader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a list of clients from file
 * Adds them like Observer. Client now will be notified if coefficient has changed
 * Runs a stock like a thread to set base coefficient and notify clients about it
 * Clients start bargaining
 * After each client bought or sold something, bargaining ends
 */
public class Runner {
    private static final Logger LOGGER = LogManager.getLogger(Runner.class);
    public static void main (String[] args) {

        LOGGER.info("Reading and creating clients from file");
        FileReader reader = new FileReader();
        Validator validator = new Validator();
        Parser parser = new Parser();
        Director director = new Director(reader, validator, parser);
        List<Client> clients = director.createClientList("./src/test/resources/clients.txt");
        LOGGER.info("List of clients has been created successfully\n");

        LOGGER.info("        ^-^-^-^-^-^ Bargaining begins ^-^-^-^-^-^  \n");

        Stock stock = Stock.getInstance();
        clients.forEach(client -> stock.addObserver(client));
        Thread stockThread = new Thread(stock, "stock");

        stockThread.start();

        try {
            stockThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Thread> clientThreads = new ArrayList<>();

        clients.forEach(client -> {
            clientThreads.add(new Thread(client, "client"+client.getId()));
        });

        clientThreads.forEach(thread -> thread.start());


        clientThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        LOGGER.info("     -^-^-^-^- All clients exited stock -^-^-^-^- \n");
        LOGGER.info("        ^-^-^-^-^-^ Bargaining ends ^-^-^-^-^-^ \n\n");

    }
}
