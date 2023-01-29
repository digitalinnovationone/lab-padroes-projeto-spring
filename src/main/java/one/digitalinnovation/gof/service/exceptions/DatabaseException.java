package one.digitalinnovation.gof.service.exceptions;

public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String msg) {
        super(msg);
    }
}