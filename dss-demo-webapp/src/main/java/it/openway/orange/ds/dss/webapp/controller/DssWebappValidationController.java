/**
 * 
 */
package it.openway.orange.ds.dss.webapp.controller;

import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;
import it.openway.orange.ds.dss.webapp.exception.DssWebappException;
import it.openway.orange.ds.dss.webapp.model.ValidateRequest;
import it.openway.orange.ds.dss.webapp.model.ValidateResponse;
import it.openway.orange.ds.dss.webapp.model.ValidateResponse.SingleValidation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.validation.executor.ValidationLevel;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.policy.rules.SubIndication;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.SimpleReport;

/**
 * @author marco Controller associato ai servizi di validazione
 */
@Controller
@RequestMapping(value = "it/iopenway/orange/ds/dss/validation")
public class DssWebappValidationController extends DssWebappBaseController {

    @Autowired
    private CertificateVerifier certificateVerifier;

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappValidationController.class);

    /**
     * Servizio di Validazione delle firme presenti in un docuemento
     * 
     * @param request
     *        ValidateRequestBean di ingresso al servizio
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return ValidateResponseBean con il risultato del servizio
     */
    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ValidateResponse validate(@RequestBody @Valid ValidateRequest request, BindingResult bindingResult) {

        ValidateResponse response = new ValidateResponse();
        List<String> bindingResultErrors = getBindingResultErrors(bindingResult);

        // Verificare se questa validazione si può intercettare in qualche altro Controller
        if (!CollectionUtils.isEmpty(bindingResultErrors)) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(bindingResultErrors);
            return response;
        }

        DSSDocument dssDocument = null;
        try {
            dssDocument = DssWebappCommonUtils.toDSSDocument(request.getBase64Document());
        }
        catch (DssWebappException e) {
            LOGGER.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        SignedDocumentValidator documentValidator = null;
        SimpleReport simpleReport = null;

        // Imposta parametri di validazione
        // NB : i parametri ocspSource e crlSource necessari per la verifica della revoca dei certificati sono impostati nell'istanza CertificateVerifier della
        // Demo
        try {
            documentValidator = SignedDocumentValidator.fromDocument(dssDocument);
            documentValidator.setCertificateVerifier(certificateVerifier);
            documentValidator.setValidationLevel(ValidationLevel.valueOf(request.getValidationLevel()));

            // Crea i report di validazione
            Reports reports = documentValidator.validateDocument();
            if (reports != null) {
                simpleReport = reports.getSimpleReport();
            }
            else {
                throw new DssWebappException("Errore nella chiamata al servizio di validazione. Non e' stato possibile generare il report");
            }
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        // Crea la response sulla base dei dati ottenuti
        response.setStatus(HttpStatus.OK);

        // NB Se il documento non contiene nessuna signature il servizio ritorna esito Negativo (di default DSS ritorna successo nella validazione)
        Indication globalResult =
            simpleReport.getSignaturesCount() == 0 ? Indication.TOTAL_FAILED : simpleReport.getValidSignaturesCount() == simpleReport.getSignaturesCount()
                ? Indication.TOTAL_PASSED : (simpleReport.getValidSignaturesCount() >= 1 ? Indication.FAILED : Indication.TOTAL_FAILED);
        response.setResult(globalResult);

        // Cicla tutte le segnature presenti nel documento, scorre le inforamzioni relative ad ognuna e le riporta sulla response. Per quelle in errore vengono
        // catturati gli errori di validazione
        ArrayList<SingleValidation> validations = new ArrayList<ValidateResponse.SingleValidation>();
        List<String> signatureIds = simpleReport.getSignatureIdList();
        for (String signatureId : signatureIds) {
            SingleValidation validation =
                new SingleValidation(
                    signatureId, simpleReport.getIndication(signatureId), simpleReport.getInfo(signatureId), simpleReport.getErrors(signatureId));
            // Se la validazione è in errore aggiunge in testa alla lista degli errori la SubIndication, utile qualora si siano verificati errori tecnici nella
            // validazione(ad es. mancanza di rete per la verifica dei certificati)
            SubIndication subIndication = null;
            if (!simpleReport.isSignatureValid(signatureId) && (subIndication = simpleReport.getSubIndication(signatureId)) != null) {
                List<String> errors = validation.getErrors();
                errors.add(0, subIndication.toString());
                validation.setErrors(errors);
            }
            validations.add(validation);
        }
        response.setValidations(validations);

        return response;
    }

}
