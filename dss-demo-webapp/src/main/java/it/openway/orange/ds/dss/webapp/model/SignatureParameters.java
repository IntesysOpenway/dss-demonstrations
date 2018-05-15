/**
 * 
 */
package it.openway.orange.ds.dss.webapp.model;

import it.openway.orange.ds.dss.webapp.validation.EnumValidation;

import java.util.List;

import javax.validation.constraints.NotNull;

import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.SignaturePackaging;

/**
 * @author marco SignatureParameters : parametri di configurazione della firma
 */
public class SignatureParameters {

    /**
     * Modalità di Packaging. Valori possibili : ENVELOPED, ENVELOPING, DETACHED
     */
    @NotNull(message = "error.signaturePackaging.notnull")
    @EnumValidation(enumClass = SignaturePackaging.class, ignoreCase = true, message = "error.signaturePackaging.enum")
    private String signaturePackaging;

    /**
     * Livello della firma include la SignatureForm (cioè la modalità di firma) e la SignatureLevel stessa che indica quale livello di standard applicare
     */
    @NotNull(message = "error.signatureLevel.notnull")
    @EnumValidation(enumClass = SignatureLevel.class, ignoreCase = true, message = "error.signatureLevel.enum")
    private String signatureLevel;

    /**
     * Algoritmo utilizzato per il calcolo del digest(Default: SHA-256)
     */
    @NotNull(message = "error.digestAlgorithm.notnull")
    @EnumValidation(enumClass = DigestAlgorithm.class, ignoreCase = true, message = "error.digestAlgorithm.enum")
    private String digestAlgorithm;

    /**
     * Flag per abilitare la firma con certificati scaduti(abilitare solo in fase di test per la firma con smartcard non valide)
     */
    private Boolean signWithExpiredCertificate = false;

    /**
     * Certificato utilizzato
     */
    @NotNull(message = "error.base64Certificate.notnull")
    private String base64Certificate;

    /**
     * Catena dei certificati estratta dal Certificato
     */
    @NotNull(message = "error.base64CertificateChain.notnull")
    private List<String> base64CertificateChain;

    /**
     * @return the signaturePackaging
     */
    public String getSignaturePackaging() {

        return signaturePackaging;
    }

    /**
     * @param signaturePackaging
     *        the signaturePackaging to set
     */
    public void setSignaturePackaging(String signaturePackaging) {

        this.signaturePackaging = signaturePackaging;
    }

    /**
     * @return the signatureLevel
     */
    public String getSignatureLevel() {

        return signatureLevel;
    }

    /**
     * @param signatureLevel
     *        the signatureLevel to set
     */
    public void setSignatureLevel(String signatureLevel) {

        this.signatureLevel = signatureLevel;
    }

    /**
     * @return the digestAlgorithm
     */
    public String getDigestAlgorithm() {

        return digestAlgorithm;
    }

    /**
     * @param digestAlgorithm
     *        the digestAlgorithm to set
     */
    public void setDigestAlgorithm(String digestAlgorithm) {

        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * @return the signWithExpiredCertificate
     */
    public Boolean getSignWithExpiredCertificate() {

        return signWithExpiredCertificate;
    }

    /**
     * @param signWithExpiredCertificate
     *        the signWithExpiredCertificate to set
     */
    public void setSignWithExpiredCertificate(Boolean signWithExpiredCertificate) {

        this.signWithExpiredCertificate = signWithExpiredCertificate;
    }

    /**
     * @return the base64Certificate
     */
    public String getBase64Certificate() {

        return base64Certificate;
    }

    /**
     * @param base64Certificate
     *        the base64Certificate to set
     */
    public void setBase64Certificate(String base64Certificate) {

        this.base64Certificate = base64Certificate;
    }

    /**
     * @return the base64CertificateChain
     */
    public List<String> getBase64CertificateChain() {

        return base64CertificateChain;
    }

    /**
     * @param base64CertificateChain
     *        the base64CertificateChain to set
     */
    public void setBase64CertificateChain(List<String> base64CertificateChain) {

        this.base64CertificateChain = base64CertificateChain;
    }

}
