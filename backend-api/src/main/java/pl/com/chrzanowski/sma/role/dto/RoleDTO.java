package pl.com.chrzanowski.sma.role.dto;

import lombok.*;
import pl.com.chrzanowski.sma.common.enumeration.ERole;

import java.time.Instant;


@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class RoleDTO {
    Long id;
    String name;
    Instant createdDatetime;
    Instant lastModifiedDatetime;
}
