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
 * @author marco Annotation utilizzata per la validazione delle Stringhe in formato Base64
 */
@Documented
@Constraint(validatedBy = {
    Base64StringValidator.class
})
@Target({
    ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Base64StringValidation {

    public abstract String message() default "Invalid value. This is not permitted.";

    public abstract Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};

}
