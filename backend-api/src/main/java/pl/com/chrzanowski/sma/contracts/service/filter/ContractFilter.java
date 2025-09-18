package pl.com.chrzanowski.sma.contracts.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

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
    private LocalDate startDateStartWith;
    private LocalDate startDateEndWith;
    private LocalDate endDateStartWith;
    private LocalDate endDateEndWith;
    private LocalDate signUpDateStartWith;
    private LocalDate signUpDateEndWith;
    private Long companyId;
    private Long constructionSiteId;
    private Long contractorId;
    private String contractorNameStartsWith;
    private String constructionSiteNameStartsWith;

}
