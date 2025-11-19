package pl.com.chrzanowski.sma.unit.dto;

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
public class UnitBaseDTO {
    protected Long id;
    protected String symbol;
    protected String description;
}
