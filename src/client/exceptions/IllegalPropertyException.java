package client.exceptions;

import server.properties.ParserProperties;

/**
 * @see ParserProperties#getProperties(String)
 */
public class IllegalPropertyException extends NullPointerException {
    public IllegalPropertyException() {super();}
}
