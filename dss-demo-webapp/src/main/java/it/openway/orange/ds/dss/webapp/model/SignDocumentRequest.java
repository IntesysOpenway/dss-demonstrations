/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import it.openway.orange.ds.dss.webapp.validation.Base64StringValidation;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author marco Request del servizio SignDocument
 */
@SuppressWarnings({
    "serial"
})
public class SignDocumentRequest extends DssWebappGenericSignRequest {

    /**
     * Data in formato ISO 8601
     */
    @NotNull(message = "error.signingDate.notnull")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date signingDate;

    /**
     * Base64 file data
     */
    @NotNull(message = "error.base64Document.notnull")
    @Base64StringValidation(message = "error.base64Document.notbase64")
    private String base64Document;

    @NotNull(message = "error.signatureValue.notnull")
    private SignatureValueAsString signatureValue;

    /**
     * @return the signatureValue
     */
    public SignatureValueAsString getSignatureValue() {

        return signatureValue;
    }

    /**
     * @param signatureValue
     *        the signatureValue to set
     */
    public void setSignatureValue(SignatureValueAsString signatureValue) {

        this.signatureValue = signatureValue;
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
