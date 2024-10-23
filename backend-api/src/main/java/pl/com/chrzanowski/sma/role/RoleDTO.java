package pl.com.chrzanowski.sma.role;

import lombok.*;
import pl.com.chrzanowski.sma.enumeration.ERole;

import java.time.Instant;


@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class RoleDTO {
    Long id;
    ERole name;
    Instant createdDatetime;
    Instant lastModifiedDatetime;
}
