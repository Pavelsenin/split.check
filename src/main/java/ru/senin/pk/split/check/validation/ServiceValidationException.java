package ru.senin.pk.split.check.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified service validation exception
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceValidationException extends RuntimeException {

    private String message;
}
