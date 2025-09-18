package pl.com.chrzanowski.sma.contracts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ContractBaseDTO {
    protected Long id;
    protected String number;
    protected String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected BigDecimal value;
    protected Currency currency;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected LocalDate signupDate;
    protected LocalDate realEndDate;
}
