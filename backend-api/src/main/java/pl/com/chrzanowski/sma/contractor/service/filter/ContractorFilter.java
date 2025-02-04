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
    private String name;
    private String taxNumber;
    private String street;
    private String buildingNo;
    private String apartmentNo;
    private String postalCode;
    private String city;
    private Country country;
    private Boolean customer;
    private Boolean supplier;
    private Boolean scaffoldingUser;
    private Instant createDateStartWith;
    private Instant createDateEndWith;
    private Instant modifyDateStartWith;
    private Instant modifyDateEndWith;
}
