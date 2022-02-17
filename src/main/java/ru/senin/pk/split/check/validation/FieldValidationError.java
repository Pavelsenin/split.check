package ru.senin.pk.split.check.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.senin.pk.split.check.controllers.responses.ErrorResponse;

/**
 * Field validation error description inside {@link ErrorResponse}
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
