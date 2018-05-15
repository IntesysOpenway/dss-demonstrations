/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author marco Request generica utilizzata nei servizi di Firma
 */
@SuppressWarnings("serial")
public abstract class DssWebappGenericSignRequest implements Serializable {

    /**
     * Parametri di impostazione della Firma
     */
    @NotNull(message = "error.signatureParameters.notnull")
    @Valid
    protected SignatureParameters signatureParameters;

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

}
