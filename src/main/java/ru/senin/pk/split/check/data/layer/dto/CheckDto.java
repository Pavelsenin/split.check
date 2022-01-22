package ru.senin.pk.split.check.data.layer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckDto {
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
    private Date date;

    /**
     * Check purchases ids
     */
    private List<PurchaseDto> purchases;

    /**
     * Check users ids
     */
    private List<UserDto> users;
}
