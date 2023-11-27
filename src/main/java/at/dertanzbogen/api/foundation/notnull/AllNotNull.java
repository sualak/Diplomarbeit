package at.dertanzbogen.api.foundation.notnull;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllNotNullValidator.class)
public @interface AllNotNull {
    String message() default "All fields must be non-null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
