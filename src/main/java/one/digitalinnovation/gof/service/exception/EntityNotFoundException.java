package one.digitalinnovation.gof.service.exception;

public class EntityNotFoundException extends RuntimeException{
    /**
     * @param msg
     */
    public EntityNotFoundException (String msg ){
        super(msg);
    }

}
