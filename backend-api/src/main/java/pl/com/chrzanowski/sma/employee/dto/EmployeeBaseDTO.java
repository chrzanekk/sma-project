package pl.com.chrzanowski.sma.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    protected final String firstName;
    @NotBlank
    protected final String lastName;
    @NotNull
    protected final BigDecimal hourRate;
}
