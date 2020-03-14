package com.teddy.log.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * ParseErrorException
 */
public class ParseErrorException extends RuntimeException {

    /**
     * ParseErrorException
     *
     * @param message string
     */
    public ParseErrorException(String message, Object... objects){
        super(MessageFormatter.arrayFormat(message, objects).getMessage());
    }
}
