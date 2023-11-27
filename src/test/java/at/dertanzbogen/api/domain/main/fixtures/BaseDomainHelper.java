package at.dertanzbogen.api.domain.main.fixtures;

import at.dertanzbogen.api.domain.main.BaseEntity;

import java.lang.reflect.Field;

public class BaseDomainHelper
{
    // Sets a private field of a BaseDomain object
    // https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
    public static <T extends BaseEntity> void setBaseDomainField(T entity, String field, Object value)
    {
        try {
            // Get the class of the entity
            var clazz = entity.getClass();

            // Get the field of the BaseDomain class
            Field declaredField = clazz.getSuperclass().getDeclaredField(field);

            // Set the field to be accessible and set the value
            declaredField.setAccessible(true);
            declaredField.set(entity, value);

        } catch (Throwable e) {
            // We should never get here
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
