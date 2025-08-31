package pl.com.chrzanowski.sma.contracts.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ContractFilter {

    private Long id;
    private String numberStartsWith;
    private String descriptionStartsWith;
    private BigDecimal valueStartsWith;
    private BigDecimal valueEndsWith;
    private Instant startDateStartWith;
    private Instant startDateEndWith;
    private Instant endDateStartWith;
    private Instant endDateEndWith;
    private Instant signUpDateStartWith;
    private Instant signUpDateEndWith;
    private Long companyId;
    private Long constructionSiteId;
    private Long contractorId;

}
