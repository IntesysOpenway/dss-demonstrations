/**
 * 
 */
package it.openway.orange.ds.dss.webapp.validation;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;

/**
 * @author marco Validator Class utilizzata attraverso l'annotation @see it.openway.orange.ds.dss.webapp.validation.Base64StringValidation utilizzata per la
 *         validazione di liste di Stringhe in formato Base64
 */
public class Base64MapValidator implements ConstraintValidator<Base64MapValidation, Map<String, String>> {

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(Base64MapValidation constraintAnnotation) {

        // DO NOTHING

    }

    @Override
    public boolean isValid(Map<String, String> map, ConstraintValidatorContext context) {
        
        if (CollectionUtils.isEmpty(map)) {
            return false;
        }
        
        for (String key : map.keySet()) {
            
            String value = map.get(key);

            if (!StringUtils.hasText(value)) {
                return false;
            }

            if (!DssWebappCommonUtils.isValidBase64(value)) {
                return false;
            }
        }

        return true;
    }

}
