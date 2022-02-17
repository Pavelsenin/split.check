package ru.senin.pk.split.check.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import ru.senin.pk.split.check.validation.FieldValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified validation error response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    private List<FieldValidationError> fieldErrors = new ArrayList<>();

    public ErrorResponse(String message) {
        this.message = message;
    }
}
