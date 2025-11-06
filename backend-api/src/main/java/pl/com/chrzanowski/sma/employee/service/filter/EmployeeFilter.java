package pl.com.chrzanowski.sma.employee.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class EmployeeFilter {

    private Long id;
    private String firstNameContains;
    private String lastNameContains;
    private String positionContains;
    private String companyContains;
    private Long companyId;
    private Long positionId;
    private BigDecimal hourRateStartsWith;
    private BigDecimal hourRateEndsWith;
}
