package pl.com.chrzanowski.sma.workshop;

import lombok.*;
import pl.com.chrzanowski.sma.enumeration.Country;

import java.time.LocalDateTime;

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
    LocalDateTime createDate;
    LocalDateTime modifyDate;
}
