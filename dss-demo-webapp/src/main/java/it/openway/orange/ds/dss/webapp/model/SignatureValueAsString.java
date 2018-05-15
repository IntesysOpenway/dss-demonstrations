package it.openway.orange.ds.dss.webapp.model;

import javax.validation.constraints.NotNull;

/**
 * @author marco Segnatura da applicare ad un documento in formato testo
 */
public class SignatureValueAsString {

    @NotNull
    private String signatureValue;

    public SignatureValueAsString() {

    }

    public SignatureValueAsString(String signatureValue) {

        this.signatureValue = signatureValue;
    }

    public String getSignatureValue() {

        return signatureValue;
    }

    public void setSignatureValue(String signatureValue) {

        this.signatureValue = signatureValue;
    }
}
