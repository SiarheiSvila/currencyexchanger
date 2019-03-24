package com.epam.currency.scanner.api;

import com.epam.currency.exceptions.FileReaderException;

import java.util.List;

public interface MyReader {
    List<String> read(String path) throws FileReaderException;
}
