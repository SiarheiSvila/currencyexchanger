package com.epam.currency.exceptions;

public class StreamClosingException extends FileReaderException {
    public StreamClosingException(String message) {
        super(message);
    }

    public StreamClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
