package pl.com.chrzanowski.sma.position.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class PositionBaseDTO {
    protected Long id;
    protected String name;
    protected String description;
}
