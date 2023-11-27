package at.dertanzbogen.api.foundation.notnull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class AllNotNullValidator implements ConstraintValidator<AllNotNull, Object>
{
    public void initialize(AllNotNull constraintAnnotation) { }

    @SneakyThrows
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        for (Field field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(value) == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Field " + field.getName() + " must be non-null")
                    .addPropertyNode(field.getName())
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}

