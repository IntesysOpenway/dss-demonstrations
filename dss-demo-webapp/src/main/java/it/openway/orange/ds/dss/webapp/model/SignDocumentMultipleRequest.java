/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author marco Request del servizio SignDocument per la versione Firma Multipla
 */
@SuppressWarnings({
    "serial"
})
public class SignDocumentMultipleRequest extends DssWebappGenericSignRequest {

    /**
     * Data in formato ISO 8601
     */
    @NotNull(message = "error.signingDate.notnull")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date signingDate;

    /**
     * Lista di SignatureData contenente i contenuti da firmare e le relative segnature
     */
    @NotNull(message = "error.signatureData.notnull")
    @Size(min = 1, max = 50, message = "error.signatureData.size")
    @Valid
    protected Map<String, SignatureData> signatureData = new HashMap<String, SignatureData>();

    public SignDocumentMultipleRequest() {
        super();
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

    public Map<String, SignatureData> getSignatureData() {

        return signatureData;
    }

    public void setSignatureData(Map<String, SignatureData> signatureData) {

        this.signatureData = signatureData;
    }

}
