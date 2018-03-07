package server.exceptions;

public class ControllerActionException extends Exception {
    public ControllerActionException() {
        super();
    }

    public ControllerActionException(String message) {
        super(message);
    }
}
