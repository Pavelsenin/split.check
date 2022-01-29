package ru.senin.pk.split.check.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified validation error response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

    private String message;

    private List<FieldValidationError> fieldErrors = new ArrayList<>();

    public ValidationErrorResponse(String message) {
        this.message = message;
    }
}
