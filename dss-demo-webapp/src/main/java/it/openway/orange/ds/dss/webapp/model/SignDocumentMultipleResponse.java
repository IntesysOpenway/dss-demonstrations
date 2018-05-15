package it.openway.orange.ds.dss.webapp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author marco Response del servizio SignDocument per la versione Firma Multipla
 */
public class SignDocumentMultipleResponse extends DssWebappGenericResponse {

    /**
     * Stringa base64 con il contenuto del file firmato
     */
    private Map<String, String> base64SignedDocuments = new HashMap<String, String>();

    public SignDocumentMultipleResponse() {
        super();
    }

    
    public Map<String, String> getBase64SignedDocuments() {
    
        return base64SignedDocuments;
    }

    
    public void setBase64SignedDocuments(Map<String, String> base64SignedDocuments) {
    
        this.base64SignedDocuments = base64SignedDocuments;
    }

}
