package one.digitalinnovation.gof.model.exceptions;

import java.io.Serializable;
import java.time.Instant;

public class Standarderror implements Serializable {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public Standarderror (){
        
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
