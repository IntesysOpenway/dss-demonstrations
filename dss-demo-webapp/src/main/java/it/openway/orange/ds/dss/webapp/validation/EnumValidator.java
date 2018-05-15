/**
 * 
 */
package it.openway.orange.ds.dss.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

/**
 * @author marco Validator Class utilizzata attraverso l'annotation @see it.openway.orange.ds.dss.webapp.validation.EnumValidation . Effettua la validazione di
 *         una Stringa validandola rispetto ad un Enumeration definita come parametro nell'annotazione
 */
public class EnumValidator implements ConstraintValidator<EnumValidation, String> {

    private EnumValidation annotation;

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    public void initialize(EnumValidation annotation) {

        this.annotation = annotation;

    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    public boolean isValid(String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {

        // / Funzione di validazione per l'annotation
        // / Ritorna true se il valore string è definito come uno dei possibili valori nell'Enumerazione target

        if (!StringUtils.hasText(valueForValidation)) {
            return this.annotation.allowEmpty();
        }

        if (!StringUtils.hasText(valueForValidation)) {
            return false;
        }

        boolean result = false;

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (valueForValidation.equals(enumValue.toString()) ||
                    (this.annotation.ignoreCase() && valueForValidation.equalsIgnoreCase(enumValue.toString()))) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
