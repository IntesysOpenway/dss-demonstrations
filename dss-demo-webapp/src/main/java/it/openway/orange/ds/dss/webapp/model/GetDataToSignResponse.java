package it.openway.orange.ds.dss.webapp.model;

/**
 * @author marco
 *  Response del servizio GetDataToSign
 */
public class GetDataToSignResponse extends DssWebappGenericResponse {

    /**
     * Data in formato ISO 8601
     */
    private String signingDate;

    /**
     * ToBeSigned risultato dell'invocazione del serivzio
     */
    private String base64ToBeSigned;

    public GetDataToSignResponse() {

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

    /**
     * @return the base64ToBeSigned
     */
    public String getBase64ToBeSigned() {

        return base64ToBeSigned;
    }

    /**
     * @param base64ToBeSigned
     *        the base64ToBeSigned to set
     */
    public void setBase64ToBeSigned(String base64ToBeSigned) {

        this.base64ToBeSigned = base64ToBeSigned;
    }

}
