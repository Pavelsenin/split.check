package ru.senin.pk.split.check.controllers.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewCheckRequest {
    /**
     * Check name
     */
    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    /**
     * Check name
     */
    @NotNull
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate date;
}
