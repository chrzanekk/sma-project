package pl.com.chrzanowski.sma.unit.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.UnitType;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class UnitBaseDTO {
    protected Long id;
    protected String symbol;
    protected UnitType unitType;
    protected String description;
}
