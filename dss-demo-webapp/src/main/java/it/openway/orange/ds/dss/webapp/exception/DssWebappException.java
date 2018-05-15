/**
 * 
 */
package it.openway.orange.ds.dss.webapp.exception;

/**
 * @author marco Exception utilizzata in Orange DS DSS Webapp
 */
@SuppressWarnings("serial")
public class DssWebappException extends Exception {

    /**
     * @param e
     */
    public DssWebappException(Exception e) {

        super(e);
    }

    /**
     * @param message
     * @param cause
     */
    public DssWebappException(String message, Exception cause) {

        super(message, cause);
    }

    /**
     * @param message
     */
    public DssWebappException(String message) {

        super(message);
    }

}
