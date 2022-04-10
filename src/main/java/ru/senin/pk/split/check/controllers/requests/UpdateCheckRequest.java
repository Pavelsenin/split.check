package ru.senin.pk.split.check.controllers.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.senin.pk.split.check.validation.constraint.validator.UniqueIds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCheckRequest {

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


    /**
     * Check users ids
     */
    @NotNull
    @Size(min = 1, max = 100)
    @UniqueIds
    private List<Long> users;
}
