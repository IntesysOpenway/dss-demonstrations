/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import it.openway.orange.ds.dss.webapp.validation.Base64StringValidation;
import it.openway.orange.ds.dss.webapp.validation.EnumValidation;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import eu.europa.esig.dss.validation.executor.ValidationLevel;

/**
 * @author marco Request del servizio di validazione
 */
public class ValidateRequest {

    /**
     * Documento da validare (in base64 encoding)
     */
    @NotNull(message = "error.base64Document.notnull")
    @Base64StringValidation(message = "error.base64Document.notbase64")
    private String base64Document;

    /**
     * Livello di validazione valori possibili : BASIC_SIGNATURES, TIMESTAMPS, LONG_TERM_DATA, ARCHIVAL_DATA Se omessa viene impostato il livello più alto
     * ARCHIVAL_DATA
     */
    @NotNull(message = "error.validationLevel.notnull")
    @EnumValidation(enumClass = ValidationLevel.class, ignoreCase = true, allowEmpty = true, message = "error.validationLevel.enum")
    private String validationLevel = ValidationLevel.ARCHIVAL_DATA.toString();

    /**
     * 
     */
    public ValidateRequest() {

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
     * @return the validationLevel
     */
    public String getValidationLevel() {

        return validationLevel;
    }

    /**
     * @param validationLevel
     *        the validationLevel to set
     */
    public void setValidationLevel(String validationLevel) {

        if (StringUtils.hasText(validationLevel)) {
            this.validationLevel = validationLevel;
        }
    }

}
