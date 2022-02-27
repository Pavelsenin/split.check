package ru.senin.pk.split.check.data.layer.entities;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendsRequestEntity {

    /**
     * Request source user id
     */
    private Long sourceUserId;

    /**
     * Request target user id
     */
    private Long targetUserId;

    /**
     * Request status
     */
    private Status status;

    @Getter
    @AllArgsConstructor
    @ToString
    public enum Status {
        PENDING("PENDING", "Pending"),
        DECLINED("DECLINED", "Accepted"),
        ACCEPTED("ACCEPTED", "Declined");

        private String code;

        private String description;

        public static Status fromCode(String code) {
            return Arrays.stream(values())
                    .filter(value -> StringUtils.equals(code, value.getCode()))
                    .findAny()
                    .orElse(null);
        }
    }
}
