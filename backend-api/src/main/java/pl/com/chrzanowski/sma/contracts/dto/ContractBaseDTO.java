package pl.com.chrzanowski.sma.contracts.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ContractBaseDTO {
    protected Long id;
    protected String number;
    protected String description;
    protected BigDecimal value;
    protected Instant startDate;
    protected Instant endDate;
    protected Instant signupDate;
    protected Instant realEndDate;
}
