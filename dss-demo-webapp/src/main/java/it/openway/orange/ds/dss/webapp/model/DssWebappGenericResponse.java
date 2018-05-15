/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * @author marco Response generica utilizzata nell'invocazione dei servizi
 */
public abstract class DssWebappGenericResponse {

    public final static String JSON_FIELD_STATUS = "status";
    public final static String JSON_FIELD_ERRORS = "errors";

    /**
     * Stato dell'invocazione del servizio
     */
    private HttpStatus status;

    /**
     * Lista degli errori presenti durante l'invocazione del servizion
     */
    private List<String> errors = new ArrayList<String>();

    /**
     * @return the stato
     */
    public HttpStatus getStatus() {

        return status;
    }

    /**
     * @param stato
     *        the stato to set
     */
    public void setStatus(HttpStatus status) {

        this.status = status;
    }

    /**
     * @return the errors
     */
    public List<String> getErrors() {

        return errors;
    }

    /**
     * @param errors
     *        the errors to set
     */
    public void setErrors(List<String> errors) {

        this.errors = errors;
    }

    public void setError(String error) {

        this.errors.add(error);
    }

}
