package myrmi.exception;

public class RemoteException extends java.io.IOException{
    // Constructor that accepts a message
    public RemoteException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
