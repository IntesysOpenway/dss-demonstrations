/**
 * 
 */
package it.openway.orange.ds.dss.webapp.validation;

import it.openway.orange.ds.dss.webapp.DssWebappCommonUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

/**
 * @author marco Validator Class utilizzata attraverso l'annotation @see it.openway.orange.ds.dss.webapp.validation.Base64StringValidation utilizzata per la
 *         validazione di Stringhe in formato Base64
 */
public class Base64StringValidator implements ConstraintValidator<Base64StringValidation, String> {

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(Base64StringValidation constraintAnnotation) {

        // DO NOTHING

    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        return DssWebappCommonUtils.isValidBase64(value);
    }

}
