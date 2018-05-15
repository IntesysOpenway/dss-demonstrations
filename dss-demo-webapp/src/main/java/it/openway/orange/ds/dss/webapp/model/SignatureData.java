/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import javax.validation.constraints.NotNull;

import it.openway.orange.ds.dss.webapp.validation.Base64StringValidation;

/**
 * @author marco SignatureData: oggetto di ingresso per la richiesta di firma multipla Serve per tenere il riferimento tra la Segnatura @link
 *         {@link SignatureValueAsString} ed il file firmato
 */
public class SignatureData {

    /**
     * Base64 file data (File firmato)
     */
    @NotNull(message = "error.base64Document.notnull")
    @Base64StringValidation(message = "error.base64Document.notbase64")
    private String base64Document;

    /**
     * Riferimento della segnatura calcolata lato client da NexU
     */
    @NotNull(message = "error.signatureValue.notnull")
    private SignatureValueAsString signatureValue;

    public SignatureData() {
        super();
    }

    public String getBase64Document() {

        return base64Document;
    }

    public void setBase64Document(String base64Document) {

        this.base64Document = base64Document;
    }

    public SignatureValueAsString getSignatureValue() {

        return signatureValue;
    }

    public void setSignatureValue(SignatureValueAsString signatureValue) {

        this.signatureValue = signatureValue;
    }

}
