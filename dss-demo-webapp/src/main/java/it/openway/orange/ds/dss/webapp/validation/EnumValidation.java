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
 * @author marco Annotation utilizzata per la validazione delle Enumerazioni (NB : in Spring 4 la classe è disponibile direttamente nel framework)
 */
@Documented
@Constraint(validatedBy = {
    EnumValidator.class
})
@Target({
    ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidation {

    public abstract String message() default "error.generic.enum";

    public abstract Class<?>[] groups() default {};

    public abstract Class<? extends Payload>[] payload() default {};

    public abstract Class<? extends java.lang.Enum<?>> enumClass();

    public abstract boolean ignoreCase() default true;
    
    public abstract boolean allowEmpty() default false;
}
