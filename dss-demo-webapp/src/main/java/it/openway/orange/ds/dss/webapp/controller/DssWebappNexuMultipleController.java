package it.openway.orange.ds.dss.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.ToBeSigned;
import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;
import it.openway.orange.ds.dss.webapp.exception.DssWebappException;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignMultipleRequest;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignMultipleResponse;
import it.openway.orange.ds.dss.webapp.model.SignDocumentMultipleRequest;
import it.openway.orange.ds.dss.webapp.model.SignDocumentMultipleResponse;
import it.openway.orange.ds.dss.webapp.service.DssWebappSigningService;

/**
 * @author marco Controller associato ai servizi di firma attraverso NexU (Versione per Firma Multipla)
 */
@Controller
@RequestMapping(value = "it/iopenway/orange/ds/dss/nexu/multiple")
public class DssWebappNexuMultipleController extends DssWebappBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappNexuMultipleController.class);

    @Autowired
    private DssWebappSigningService dssWebappSigningService;

    /**
     * Servizio di pre-firma lato server. Ritorna il 'dataToSign' cioè il digest calcolato sul contenuto del file da firmare attraverso il token della SmartCard
     * Versione per Firma Multipla
     * 
     * @param request
     *        GetDataToSignMultipleRequest che rappresenta la richiesta di ingresso al servizio
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return GetDataToSignMultipleResponse che rappresenta il risultato dell'invocazione del servizio
     * @throws DssWebappException
     */
    @RequestMapping(value = "/getDataToSign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GetDataToSignMultipleResponse getDataToSignMultiple(@RequestBody @Valid GetDataToSignMultipleRequest request, BindingResult bindingResult)
        throws DssWebappException {

        GetDataToSignMultipleResponse response = new GetDataToSignMultipleResponse();
        List<String> bindingResultErrors = getBindingResultErrors(bindingResult);

        // Verificare se questa validazione si può intercettare in qualche altro Controller
        if (!CollectionUtils.isEmpty(bindingResultErrors)) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(bindingResultErrors);
            return response;
        }

        Map<String, ToBeSigned> toBeSigneds = new HashMap<String, ToBeSigned>();
        try {
            toBeSigneds = dssWebappSigningService.getDataToSignMultiple(request);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        // Conversione alla response del servizio: rimappa i ToBeSigned codificandoli in Stringa base 64
        Map<String, String> base64ToBeSigneds = new HashMap<String, String>();
        for (String key : toBeSigneds.keySet()) {

            ToBeSigned toBeSigned = toBeSigneds.get(key);
            String base64ToBeSigned = DatatypeConverter.printBase64Binary(toBeSigned.getBytes());
            base64ToBeSigneds.put(key, base64ToBeSigned);
        }

        response.setBase64ToBeSigneds(base64ToBeSigneds);
        // NB Viene inclusa la data di firma alla response dato che è un dato di input del servizio SignDocument
        response.setSigningDate(DssWebappCommonUtils.formatISO8601Date(request.getSigningDate()));
        response.setStatus(HttpStatus.OK);

        return response;

    }

    /**
     * Servizio di firma di un documento. Ritorna il contenuto del documento firmato Versione per Firma Multipla
     * 
     * @param request
     *        SignDocumentRequest che rappresenta la richiesta di ingresso al servizio
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return SignDocumentResponse che rappresenta il risultato dell'invocazione del servizio
     */
    @RequestMapping(value = "/signDocument", method = RequestMethod.POST)
    @ResponseBody
    public SignDocumentMultipleResponse signDocumentMultiple(@RequestBody @Validated SignDocumentMultipleRequest request, BindingResult bindingResult) {

        SignDocumentMultipleResponse response = new SignDocumentMultipleResponse();
        List<String> bindingResultErrors = getBindingResultErrors(bindingResult);

        // Verificare se questa validazione si può intercettare in qualche altro Controller
        if (!CollectionUtils.isEmpty(bindingResultErrors)) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(bindingResultErrors);
            return response;
        }

        Map<String, DSSDocument> signedDSSDocuments = new HashMap<String, DSSDocument>();
        try {
            signedDSSDocuments = dssWebappSigningService.signDocumentMultiple(request);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        // Viene convertita la mappa di DssDocuments nella mappa della response contenenti le Stringhe base 64
        Map<String, String> base64SignedDocuments = new HashMap<String, String>();

        for (String key : signedDSSDocuments.keySet()) {

            DSSDocument dssDocument = signedDSSDocuments.get(key);
            String sBase64SignedDocument = null;

            try {
                byte[] bytesSignedDocument = DSSUtils.toByteArray(dssDocument);
                sBase64SignedDocument = DssWebappCommonUtils.encodeBase64(bytesSignedDocument);
            }
            catch (DssWebappException e) {
                LOGGER.error(e.getMessage(), e);
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setError(e.getMessage());
                return response;
            }

            base64SignedDocuments.put(key, sBase64SignedDocument);
        }

        response.setBase64SignedDocuments(base64SignedDocuments);
        response.setStatus(HttpStatus.OK);

        return response;
    }

}
