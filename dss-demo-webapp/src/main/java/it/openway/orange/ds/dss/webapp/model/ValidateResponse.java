/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.validation.policy.rules.Indication;

/**
 * @author marco Response del servizio di validazione
 */
public class ValidateResponse extends DssWebappGenericResponse {

    /**
     * Esito globale della validazione dell'intero documento
     */
    private Indication result;

    /**
     * Lista delle validazioni rispetto ad ogni Segnatura presente nel documento
     */
    private List<SingleValidation> validations;

    /**
     * @param result
     * @param validations
     */
    public ValidateResponse(Indication result, List<SingleValidation> validations) {

        super();
        this.result = result;
        this.validations = validations;
    }

    /**
     * @author marco Esito ed indicazioni relative ad ogni singola Segnatura presente nel documento
     */
    public static class SingleValidation {

        /**
         * SignatureId
         */
        private String signatureId;

        /**
         * Esito della validazione della singola segnatura
         */
        private Indication result;

        /**
         * Lista delle informazioni relative alla segnatura ritornate dal processo di validazione
         */
        private List<String> info;

        /**
         * Se la segnatura non è valida viene popolata con la lista degli errori generati dalla segnatura
         */
        private List<String> errors;

        /**
         * @param signatureId
         * @param result
         */
        public SingleValidation(String signatureId, Indication result) {

            super();
            this.signatureId = signatureId;
            this.result = result;
            info = new ArrayList<String>();
            errors = new ArrayList<String>();
        }

        /**
         * @param signatureId
         * @param result
         * @param info
         * @param errors
         */
        public SingleValidation(String signatureId, Indication result, List<String> info, List<String> errors) {

            super();
            this.signatureId = signatureId;
            this.result = result;
            this.info = info;
            this.errors = errors;
        }

        /**
         * @return the signatureId
         */
        public String getSignatureId() {

            return signatureId;
        }

        /**
         * @param signatureId
         *        the signatureId to set
         */
        public void setSignatureId(String signatureId) {

            this.signatureId = signatureId;
        }

        /**
         * @return the result
         */
        public Indication getResult() {

            return result;
        }

        /**
         * @param result
         *        the result to set
         */
        public void setResult(Indication result) {

            this.result = result;
        }

        /**
         * @return the info
         */
        public List<String> getInfo() {

            return info;
        }

        /**
         * @param info
         *        the info to set
         */
        public void setInfo(List<String> info) {

            this.info = info;
        }

        /**
         * @return the errors
         */
        public List<String> getErrors() {

            return errors;
        }

        /**
         * @param errors
         *        the errors to set
         */
        public void setErrors(List<String> errors) {

            this.errors = errors;
        }

    }

    public ValidateResponse() {

        super();
    }

    /**
     * @return the result
     */
    public Indication getResult() {

        return result;
    }

    /**
     * @param result
     *        the result to set
     */
    public void setResult(Indication result) {

        this.result = result;
    }

    /**
     * @return the validations
     */
    public List<SingleValidation> getValidations() {

        return validations;
    }

    /**
     * @param validations
     *        the validations to set
     */
    public void setValidations(List<SingleValidation> validations) {

        this.validations = validations;
    }

}
