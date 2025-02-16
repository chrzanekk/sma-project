package pl.com.chrzanowski.sma.contractor.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.time.Instant;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ContractorFilter {
    private Long id;
    private String nameStartsWith;
    private String taxNumberStartsWith;
    private String streetStartsWith;
    private String buildingNoStartsWith;
    private String apartmentNoStartsWith;
    private String postalCodeStartsWith;
    private String cityStartsWith;
    private Country country;
    private Boolean customer;
    private Boolean supplier;
    private Boolean scaffoldingUser;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;
}
