package ru.senin.pk.split.check.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Field validation error description inside {@link ValidationErrorResponse}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldValidationError {

    private String field = "";

    private String message = "";

    public FieldValidationError(String message) {
        this.message = message;
    }
}
