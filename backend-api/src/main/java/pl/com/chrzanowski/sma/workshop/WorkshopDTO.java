package pl.com.chrzanowski.sma.workshop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.com.chrzanowski.sma.enumeration.Country;

import java.time.Instant;

@Builder(toBuilder = true)
@AllArgsConstructor
@Value
public class WorkshopDTO {
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
