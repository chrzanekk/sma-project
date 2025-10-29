package pl.com.chrzanowski.sma.employee.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class EmployeeBaseDTO {
    protected Long id;
    protected final String firstName;
    protected final String lastName;
    protected final BigDecimal hourRate;
}
