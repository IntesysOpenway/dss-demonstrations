/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.openway.orange.ds.dss.webapp.validation.Base64MapValidation;

/**
 * @author marco Request di ingresso per il servizio GetDataToSign per la versione Firma Multipla NB : I Campi sono dichiarati di tipo String e non
 *         dell'Enumeration perchè altrimenti era necessario un override delle Enumerazioni con l'aggiunta di metodi di validazioni
 */
@SuppressWarnings("serial")
public class GetDataToSignMultipleRequest extends DssWebappGenericSignRequest {

    /**
     * Lista dei documenti da firmare (in base64 encoding)
     */
    @NotNull(message = "error.base64Documents.notnull")
    @Size(min = 1, max = 50, message = "error.base64Documents.size")
    @Base64MapValidation(message = "error.base64Documents.notbase64")
    private Map<String, String> base64Documents = new HashMap<String, String>();

    /**
     * Data di firma (di default viene impostata all'invocazione della request)
     */
    private Date signingDate = new Date();

    public GetDataToSignMultipleRequest() {

        super();
    }

    public Map<String, String> getBase64Documents() {

        return base64Documents;
    }

    public void setBase64Documents(Map<String, String> base64Documents) {

        this.base64Documents = base64Documents;
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
