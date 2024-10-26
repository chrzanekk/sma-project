package pl.com.chrzanowski.sma.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.time.Instant;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
public class ContractorDTO {
    Long id;
    String name;
    String taxNumber;
    String street;
    String buildingNo;
    String apartmentNo;
    String postalCode;
    String city;
    Country country;
    Instant createdDatetime;
    Instant lastModifiedDatetime;
}
