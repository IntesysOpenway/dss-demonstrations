package it.openway.orange.ds.dss.webapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.europa.esig.dss.AbstractSignatureParameters;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;
import eu.europa.esig.dss.SignatureAlgorithm;
import eu.europa.esig.dss.SignatureForm;
import eu.europa.esig.dss.SignatureLevel;
import eu.europa.esig.dss.SignaturePackaging;
import eu.europa.esig.dss.SignatureValue;
import eu.europa.esig.dss.ToBeSigned;
import eu.europa.esig.dss.cades.CAdESSignatureParameters;
import eu.europa.esig.dss.cades.signature.CAdESService;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;
import it.openway.orange.ds.dss.webapp.exception.DssWebappException;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignMultipleRequest;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignRequest;
import it.openway.orange.ds.dss.webapp.model.SignDocumentMultipleRequest;
import it.openway.orange.ds.dss.webapp.model.SignDocumentRequest;
import it.openway.orange.ds.dss.webapp.model.SignatureData;
import it.openway.orange.ds.dss.webapp.model.SignatureParameters;

/**
 * @author marco Servizio intermedio che processa i parametri dalle request ed inoltra all'appropriato servizio DSS
 */
@Component
public class DssWebappSigningService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappSigningService.class);

    @Autowired
    private CAdESService dssWebappCadesService;

    @Autowired
    private PAdESService dssWebappPadesService;

    @Autowired
    private XAdESService dssWebappXadesService;

    /**
     * Serviziio GetDataToSign
     * 
     * @param request
     *        Request del servizio
     * @return ToBeSigned oggetto DSS che rappresenta l'hash calcolato dal servizio
     * @throws DssWebappException
     */
    @SuppressWarnings("unchecked")
    public ToBeSigned getDataToSign(GetDataToSignRequest request)
        throws DssWebappException {

        ToBeSigned toBeSigned = null;
        @SuppressWarnings("rawtypes")
        DocumentSignatureService service = null;
        DSSDocument dssDocumentToSign = null;
        AbstractSignatureParameters dssSignatureParameters = null;

        try {
            dssSignatureParameters = getDSSSignatureParameters(request.getSignatureParameters(), request.getSigningDate());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nell'estrazione dei parametri dalla DataToSignRequest", e);
        }

        try {
            service = getSignatureService(dssSignatureParameters.getSignatureLevel().getSignatureForm());
        }
        catch (Exception e) {
            throw new DssWebappException(String.format(
                "Errore nel determinare il SignatureService corrispondente alla SignatureForm %s",
                dssSignatureParameters.getSignatureLevel().getSignatureForm()), e);
        }

        try {
            dssDocumentToSign = DssWebappCommonUtils.toDSSDocument(request.getBase64Document());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nella costruzione del DSSDocument rispetto al documento in input", e);
        }

        try {
            toBeSigned = service.getDataToSign(dssDocumentToSign, dssSignatureParameters);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e );
            throw e;
        }

        return toBeSigned;
    }

    /**
     * Servizio SignDocument
     * 
     * @param signDocumentRequest
     *        Request del servizio
     * @return DSSDocument contenente il documento firmato
     * @throws DssWebappException
     */
    @SuppressWarnings("unchecked")
    public DSSDocument signDocument(SignDocumentRequest signDocumentRequest)
        throws DssWebappException {

        @SuppressWarnings("rawtypes")
        DocumentSignatureService service = null;
        DSSDocument dssDocumentToSign = null;
        AbstractSignatureParameters dssSignatureParameters = null;

        try {
            dssSignatureParameters = getDSSSignatureParameters(signDocumentRequest.getSignatureParameters(), signDocumentRequest.getSigningDate());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nell'estrazione dei parametri dalla DataToSignRequest", e);
        }

        try {
            service = getSignatureService(dssSignatureParameters.getSignatureLevel().getSignatureForm());
        }
        catch (Exception e) {
            throw new DssWebappException(String.format(
                "Errore nel determinare il SignatureService corrispondente alla SignatureForm %s",
                dssSignatureParameters.getSignatureLevel().getSignatureForm()), e);
        }

        try {

            dssDocumentToSign = DssWebappCommonUtils.toDSSDocument(signDocumentRequest.getBase64Document());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nella costruzione del DSSDocument rispetto al documentToSign", e);
        }

        // L'algoritmo di firma è la combinazione tra l'algoritmo di encriptionAlgorithm e il digestAlgorithm (es: "SHA1withRSA")
        SignatureAlgorithm signatureAlgorithm =
            SignatureAlgorithm.getAlgorithm(dssSignatureParameters.getEncryptionAlgorithm(), dssSignatureParameters.getDigestAlgorithm());

        // Determina i bytes della Segnatura
        byte[] bytesSignature = DatatypeConverter.parseBase64Binary(signDocumentRequest.getSignatureValue().getSignatureValue());
        SignatureValue signatureValue = new SignatureValue(signatureAlgorithm, bytesSignature);

        DSSDocument signedDocument = null;
        try {
            signedDocument = service.signDocument(dssDocumentToSign, dssSignatureParameters, signatureValue);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        return signedDocument;
    }
    
    /**
     * Servizio GetDataToSignMultiple
     * 
     * @param request
     *        Request del servizio della webapp
     * @return ToBeSigned Mappa degli oggetti DSS che rappresentano l'hash calcolato dal servizio
     * @throws DssWebappException
     */
    @SuppressWarnings("unchecked")
    public Map<String, ToBeSigned> getDataToSignMultiple(GetDataToSignMultipleRequest request)
        throws DssWebappException {

        HashMap<String, ToBeSigned> toBeSigneds = new HashMap<String, ToBeSigned>();

        @SuppressWarnings("rawtypes")
        DocumentSignatureService service = null;

        AbstractSignatureParameters dssSignatureParameters = null;

        try {
            dssSignatureParameters = getDSSSignatureParameters(request.getSignatureParameters(), request.getSigningDate());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nell'estrazione dei parametri dalla DataToSignRequest", e);
        }

        try {
            service = getSignatureService(dssSignatureParameters.getSignatureLevel().getSignatureForm());
        }
        catch (Exception e) {
            throw new DssWebappException(
                String.format(
                    "Errore nel determinare il SignatureService corrispondente alla SignatureForm %s",
                    dssSignatureParameters.getSignatureLevel().getSignatureForm()),
                e);
        }

        for (String key : request.getBase64Documents().keySet()) {

            String sBase64Document = request.getBase64Documents().get(key);
            DSSDocument dssDocumentToSign = null;
            try {
                dssDocumentToSign = DssWebappCommonUtils.toDSSDocument(sBase64Document);
            }
            catch (Exception e) {
                throw new DssWebappException("Errore nella costruzione del DSSDocument rispetto al documento in input", e);
            }

            ToBeSigned toBeSigned = null;

            try {
                toBeSigned = service.getDataToSign(dssDocumentToSign, dssSignatureParameters);
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }

            toBeSigneds.put(key, toBeSigned);
        }

        return toBeSigneds;
    }

    /**
     * Servizio SignDocumentMultiple
     * 
     * @param signDocumentMultipleRequest
     *        Request del servizio
     * @return Mappa di DSSDocument ognuno contenente il relativo documento firmato
     * @throws DssWebappException
     */
    @SuppressWarnings("unchecked")
    public Map<String, DSSDocument> signDocumentMultiple(SignDocumentMultipleRequest signDocumentRequest)
        throws DssWebappException {

        @SuppressWarnings("rawtypes")
        DocumentSignatureService service = null;
        AbstractSignatureParameters dssSignatureParameters = null;

        try {
            dssSignatureParameters = getDSSSignatureParameters(signDocumentRequest.getSignatureParameters(), signDocumentRequest.getSigningDate());
        }
        catch (Exception e) {
            throw new DssWebappException("Errore nell'estrazione dei parametri dalla DataToSignRequest", e);
        }

        try {
            service = getSignatureService(dssSignatureParameters.getSignatureLevel().getSignatureForm());
        }
        catch (Exception e) {
            throw new DssWebappException(
                String.format(
                    "Errore nel determinare il SignatureService corrispondente alla SignatureForm %s",
                    dssSignatureParameters.getSignatureLevel().getSignatureForm()),
                e);
        }

        // L'algoritmo di firma è la combinazione tra l'algoritmo di encriptionAlgorithm e il digestAlgorithm (es: "SHA1withRSA")
        SignatureAlgorithm signatureAlgorithm =
            SignatureAlgorithm.getAlgorithm(dssSignatureParameters.getEncryptionAlgorithm(), dssSignatureParameters.getDigestAlgorithm());

        Map<String, DSSDocument> signedDocuments = new HashMap<String, DSSDocument>();

        Map<String, SignatureData> signatureDatas = signDocumentRequest.getSignatureData();
        for (String key : signatureDatas.keySet()) {

            SignatureData signatureData = signatureDatas.get(key);
            DSSDocument dssDocumentToSign = null;

            try {
                dssDocumentToSign = DssWebappCommonUtils.toDSSDocument(signatureData.getBase64Document());
            }
            catch (Exception e) {
                throw new DssWebappException("Errore nella costruzione del DSSDocument rispetto al documentToSign", e);
            }

            // Determina i bytes della Segnatura
            byte[] bytesSignature = DatatypeConverter.parseBase64Binary(signatureData.getSignatureValue().getSignatureValue());
            SignatureValue signatureValue = new SignatureValue(signatureAlgorithm, bytesSignature);

            DSSDocument signedDocument = null;
            try {
                signedDocument = service.signDocument(dssDocumentToSign, dssSignatureParameters, signatureValue);
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }

            signedDocuments.put(key, signedDocument);
        }

        return signedDocuments;
    }

    /**
     * Estrae i parametri di firma nell'oggetto AbstractSignatureParameters utilizzato dalle librerie DSS per l'elaborazione
     * 
     * @param signatureParameters
     *        SignatureParameters definiti in Orange DS DSS Webapp
     * @param signingDate
     *        Data di firma utilizzata nei servizi GetDataToSign e SignDocument
     * @return l'istanza specializzata di AbstractSignatureParameters
     * @throws DssWebappException
     */
    public static AbstractSignatureParameters getDSSSignatureParameters(SignatureParameters signatureParameters, Date signingDate)
        throws DssWebappException {

        // Estrae dalla SignatureLevel i parametri SignatureForm e SignatureLevel
        SignatureLevel signatureLevel = SignatureLevel.valueByName((signatureParameters.getSignatureLevel()));
        SignatureForm signatureForm = signatureLevel.getSignatureForm();

        AbstractSignatureParameters parameters = null;

        switch (signatureForm) {
        case CAdES:
            parameters = new CAdESSignatureParameters();
            break;
        case PAdES:
            PAdESSignatureParameters padesParams = new PAdESSignatureParameters();
            padesParams.setSignatureSize(9472 * 2); // double reserved space for signature
            parameters = padesParams;
            break;
        case XAdES:
            parameters = new XAdESSignatureParameters();
            break;
        default:
            LOGGER.error(String.format("SignatureForm sconosciuta: %s", signatureForm));
            throw new DssWebappException(String.format("SignatureForm sconosciuta: %s", signatureForm));
        }

        parameters.setSignatureLevel(signatureLevel);
        parameters.setSignaturePackaging(SignaturePackaging.valueOf(signatureParameters.getSignaturePackaging()));
        parameters.setDigestAlgorithm(DigestAlgorithm.valueOf(signatureParameters.getDigestAlgorithm()));

        // Parametri B-Level(Base Level)
        parameters.bLevel().setSigningDate(signingDate);

        // Caricamento del Certificato
        CertificateToken signingCertificate = DSSUtils.loadCertificateFromBase64EncodedString(signatureParameters.getBase64Certificate());
        parameters.setSigningCertificate(signingCertificate);

        // NB : l'algoritmo di codifica viene determinato dal certificato
        parameters.setEncryptionAlgorithm(signingCertificate.getEncryptionAlgorithm());

        // Caricamento della Catena di Certificati
        if (CollectionUtils.isNotEmpty(signatureParameters.getBase64CertificateChain())) {
            Set<CertificateToken> certificateChain = new HashSet<CertificateToken>();
            for (String sBase64CertificateChain : signatureParameters.getBase64CertificateChain()) {
                certificateChain.add(DSSUtils.loadCertificateFromBase64EncodedString(sBase64CertificateChain));
            }
            List<CertificateToken> lstCertificateChain = new ArrayList<CertificateToken>(certificateChain);
            parameters.setCertificateChain(lstCertificateChain);
        }

        parameters.setSignWithExpiredCertificate(signatureParameters.getSignWithExpiredCertificate());

        return parameters;

    }

    /**
     * Ritorna il DocumentSignatureService DSS appropriato a seconda della SignatureForm utilizzata
     * 
     * @param signatureForm
     *        SignatureForm utilizzata
     * @return l'istanza del DocumentSignatureService appropriata
     */
    @SuppressWarnings("rawtypes")
    public DocumentSignatureService getSignatureService(SignatureForm signatureForm) {

        DocumentSignatureService service = null;
        switch (signatureForm) {
        case CAdES:
            service = dssWebappCadesService;
            break;
        case PAdES:
            service = dssWebappPadesService;
            break;
        case XAdES:
            service = dssWebappXadesService;
            break;
        default:
            LOGGER.error("Unknow signature form : " + signatureForm);
        }
        return service;
    }

}
