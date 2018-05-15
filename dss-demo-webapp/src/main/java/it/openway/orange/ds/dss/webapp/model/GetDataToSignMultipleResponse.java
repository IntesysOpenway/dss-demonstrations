package it.openway.orange.ds.dss.webapp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author marco Response del servizio GetDataToSignMultiple
 */
public class GetDataToSignMultipleResponse extends DssWebappGenericResponse {

    /**
     * Data in formato ISO 8601
     */
    private String signingDate;

    /**
     * ToBeSigned risultato dell'invocazione del serivzio
     */
    private Map<String, String> base64ToBeSigneds = new HashMap<String, String>();

    public GetDataToSignMultipleResponse() {

        super();
    }

    /**
     * @return the signingDate
     */
    public String getSigningDate() {

        return signingDate;
    }

    /**
     * @param signingDate
     *        the signingDate to set
     */
    public void setSigningDate(String signingDate) {

        this.signingDate = signingDate;
    }

    public Map<String, String> getBase64ToBeSigneds() {

        return base64ToBeSigneds;
    }

    public void setBase64ToBeSigneds(Map<String, String> base64ToBeSigneds) {

        this.base64ToBeSigneds = base64ToBeSigneds;
    }

}
