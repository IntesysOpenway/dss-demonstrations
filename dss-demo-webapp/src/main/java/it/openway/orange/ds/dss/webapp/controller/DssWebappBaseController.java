/**
 * 
 */
package it.openway.orange.ds.dss.webapp.controller;

import it.openway.orange.ds.dss.webapp.model.DssWebappGenericResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author marco Base Controller per Orange DS DSS Webapp
 */
public class DssWebappBaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssWebappBaseController.class);

    @Autowired
    @Qualifier("dssWebappMessageSource")
    private MessageSource dssWebappMessageSource;

    /**
     * Funzione che ritorna la lista degli errori di validazione sugli oggetti Request in ingresso ai Controller
     * 
     * @param bindingResult
     *        BindingResult corrente contenente gli errori di validazione
     * @return Lista delle etichette associate agli errori di validazione
     */
    protected List<String> getBindingResultErrors(BindingResult bindingResult) {

        List<String> bindingErrors = new ArrayList<String>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        if (CollectionUtils.isEmpty(allErrors)) {
            return bindingErrors;
        }

        for (ObjectError error : allErrors) {
            String message = dssWebappMessageSource.getMessage(error.getDefaultMessage(), null, Locale.getDefault());
            bindingErrors.add(message);
        }

        return bindingErrors;
    }

    /**
     * Handler generico di gestione degli errori: viene invocato di default nei controller che sono estesi da questa classe. In maniera da non interferire con
     * l'Handler della Webapp di Demo
     * 
     * @param e
     *        Eccezione catturata
     * @return Una Response in formato JSON con l'errore catturato
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @RequestMapping(produces = "application/json")
    @ResponseBody
    public Map<String, Object> handleGenericExceptionAsJSON(Exception e) {

        LOGGER.error(e.getMessage(), e);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DssWebappGenericResponse.JSON_FIELD_STATUS, HttpStatus.BAD_REQUEST);

        ArrayList<String> lstErrors = new ArrayList<String>();
        lstErrors.add(e.getMessage());
        map.put(DssWebappGenericResponse.JSON_FIELD_ERRORS, lstErrors);

        return map;
    }

}
