package pl.com.chrzanowski.sma.employee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeDTO extends EmployeeBaseDTO implements HasId<Long> {
    @NotNull
    private CompanyBaseDTO company;
    @NotNull
    private PositionBaseDTO position;
}
