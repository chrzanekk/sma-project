package pl.com.chrzanowski.scma.util;

import pl.com.chrzanowski.scma.exception.EmptyValueException;
import pl.com.chrzanowski.scma.exception.ObjectNotFoundException;

import java.util.Set;

public class FieldValidator {

    public static void validateString(String field, String fieldType) {
        if(field == null || field.isEmpty()) {
            throw new EmptyValueException("Field cant be empty: " + fieldType);
        }
    }

    public static void validateObject(Object object, String objectType) {
        if(object == null) {
            throw new ObjectNotFoundException("Parameter is null: " + objectType);
        }
    }

    public static void validateSet(Set<?> object, String objectType) {
        if(object == null) {
            throw new ObjectNotFoundException("Parameter is null: " + objectType);
        }
    }

}
