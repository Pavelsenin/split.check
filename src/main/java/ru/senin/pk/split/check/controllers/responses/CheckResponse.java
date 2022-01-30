package ru.senin.pk.split.check.controllers.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckResponse {
    /**
     * Check id
     */
    private Long id;

    /**
     * Check name
     */
    private String name;

    /**
     * Check date
     */
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate date;

    /**
     * Check purchases
     */
    private List<Long> purchases;

    /**
     * Check users
     */
    private List<Long> users;
}
