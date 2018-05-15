/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import it.openway.orange.ds.dss.webapp.validation.Base64StringValidation;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * @author marco Request di ingresso per il servizio GetDataToSign NB : I Campi sono dichiarati di tipo String e non dell'Enumeration perchè altrimenti era
 *         necessario un override delle Enumerazioni con l'aggiunta di metodi di validazioni
 */
@SuppressWarnings("serial")
public class GetDataToSignRequest extends DssWebappGenericSignRequest {

    /**
     * Documento da firmare (in base64 encoding)
     */
    @NotNull(message = "error.base64Document.notnull")
    @Base64StringValidation(message = "error.base64Document.notbase64")
    private String base64Document;

    /**
     * Data di firma (di default viene impostata all'invocazione della request)
     */
    private Date signingDate = new Date();

    public GetDataToSignRequest() {

        super();
    }

    /**
     * @return the base64Document
     */
    public String getBase64Document() {

        return base64Document;
    }

    /**
     * @param base64Document
     *        the base64Document to set
     */
    public void setBase64Document(String base64Document) {

        this.base64Document = base64Document;
    }

    /**
     * @return the signatureParameters
     */
    public SignatureParameters getSignatureParameters() {

        return signatureParameters;
    }

    /**
     * @param signatureParameters
     *        the signatureParameters to set
     */
    public void setSignatureParameters(SignatureParameters signatureParameters) {

        this.signatureParameters = signatureParameters;
    }

    /**
     * @return the signingDate
     */
    public Date getSigningDate() {

        return signingDate;
    }

    /**
     * @param signingDate
     *        the signingDate to set
     */
    public void setSigningDate(Date signingDate) {

        this.signingDate = signingDate;
    }

}
