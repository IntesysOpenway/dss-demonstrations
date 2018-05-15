package it.openway.orange.ds.dss.webapp.controller;

import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;
import it.openway.orange.ds.dss.webapp.exception.DssWebappException;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignRequest;
import it.openway.orange.ds.dss.webapp.model.GetDataToSignResponse;
import it.openway.orange.ds.dss.webapp.model.SignDocumentRequest;
import it.openway.orange.ds.dss.webapp.model.SignDocumentResponse;
import it.openway.orange.ds.dss.webapp.service.DssWebappSigningService;

import java.util.List;

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

/**
 * @author marco Controller associato ai servizi di firma attraverso NexU
 */
@Controller
@RequestMapping(value = "it/iopenway/orange/ds/dss/nexu")
public class DssWebappNexuController extends DssWebappBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappNexuController.class);

    @Autowired
    private DssWebappSigningService dssWebappSigningService;

    /**
     * Servizio di pre-firma lato server. Ritorna il 'dataToSign' cioè il digest calcolato sul contenuto del file da firmare attraverso il token della SmartCard
     * 
     * @param request
     *        GetDataToSignRequest che rappresenta la richiesta di ingresso al servizio
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return GetDataToSignResponse che rappresenta il risultato dell'invocazione del servizio
     * @throws DssWebappException
     */
    @RequestMapping(value = "/getDataToSign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public GetDataToSignResponse getDataToSign(@RequestBody @Valid GetDataToSignRequest request, BindingResult bindingResult)
        throws DssWebappException {

        GetDataToSignResponse response = new GetDataToSignResponse();
        List<String> bindingResultErrors = getBindingResultErrors(bindingResult);

        // Verificare se questa validazione si può intercettare in qualche altro Controller
        if (!CollectionUtils.isEmpty(bindingResultErrors)) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(bindingResultErrors);
            return response;
        }

        ToBeSigned dataToSign = null;
        try {
            dataToSign = dssWebappSigningService.getDataToSign(request);

            response.setStatus(HttpStatus.OK);
            response.setBase64ToBeSigned(DatatypeConverter.printBase64Binary(dataToSign.getBytes()));
            // NB Viene inclusa la data di firma alla response dato che è un dato di input del servizio SignDocument
            response.setSigningDate(DssWebappCommonUtils.formatISO8601Date(request.getSigningDate()));

            return response;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }
    }

    /**
     * Servizio di firma di un documento. Ritorna il contenuto del documento firmato
     * 
     * @param request
     *        SignDocumentRequest che rappresenta la richiesta di ingresso al servizio
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return SignDocumentResponse che rappresenta il risultato dell'invocazione del servizio
     */
    @RequestMapping(value = "/signDocument", method = RequestMethod.POST)
    @ResponseBody
    public SignDocumentResponse signDocument(@RequestBody @Validated SignDocumentRequest request, BindingResult bindingResult) {

        SignDocumentResponse response = new SignDocumentResponse();
        List<String> bindingResultErrors = getBindingResultErrors(bindingResult);

        // Verificare se questa validazione si può intercettare in qualche altro Controller
        if (!CollectionUtils.isEmpty(bindingResultErrors)) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(bindingResultErrors);
            return response;
        }

        DSSDocument signedDocument = null;
        try {
            signedDocument = dssWebappSigningService.signDocument(request);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        // InMemoryDocument imSignedDocument = new InMemoryDocument(DSSUtils.toByteArray(signedDocument), signedDocument.getName(),
        // signedDocument.getMimeType());

        String base64SignedDocument = null;
        SignDocumentResponse signDocumentResponse = new SignDocumentResponse();

        try {
            byte[] bytesSignedDocument = DSSUtils.toByteArray(signedDocument);
            base64SignedDocument = DssWebappCommonUtils.encodeBase64(bytesSignedDocument);

            signDocumentResponse.setStatus(HttpStatus.OK);
            signDocumentResponse.setBase64SignedDocument(base64SignedDocument);
        }
        catch (DssWebappException e) {
            LOGGER.error(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(e.getMessage());
            return response;
        }

        return signDocumentResponse;
    }

}
