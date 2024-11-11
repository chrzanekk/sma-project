package pl.com.chrzanowski.sma.role.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

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
