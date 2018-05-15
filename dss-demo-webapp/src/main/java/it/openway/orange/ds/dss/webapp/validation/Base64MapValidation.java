/**
 * 
 */
package it.openway.orange.ds.dss.webapp.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author marco Annotation utilizzata per la validazione di una lista contenente Stringhe in formato Base64
 */
@Documented
@Constraint(validatedBy = {
    Base64MapValidator.class
})
@Target({
    ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64MapValidation {

    public abstract String message() default "Invalid value. This is not permitted.";

    public abstract Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};

}
