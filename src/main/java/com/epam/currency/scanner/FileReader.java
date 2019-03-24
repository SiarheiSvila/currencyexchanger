package com.epam.currency.scanner;

import com.epam.currency.control.Director;
import com.epam.currency.exceptions.FileException;
import com.epam.currency.exceptions.FileReaderException;
import com.epam.currency.exceptions.StreamClosingException;
import com.epam.currency.scanner.api.MyReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads data from file and returns it as a List<String>
 */

public class FileReader implements MyReader {

    final private static Logger LOGGER = Logger.getLogger(Director.class.getName());

    public List<String> read(String path) throws FileReaderException {

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(path);

            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            List<String> list = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            return list;
        } catch (FileNotFoundException e) {       //To define thar error is concretely in missing file
            LOGGER.error("File is not found", e);
            throw new FileException("File is not found", e);

        } catch (IOException e) {
            LOGGER.error("Error in reading from file", e);
            throw new FileException("Error in reading from file", e);

        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("Error in stream closing", e);
                throw new StreamClosingException("Error in stream closing", e);
            }
        }

    }
}
