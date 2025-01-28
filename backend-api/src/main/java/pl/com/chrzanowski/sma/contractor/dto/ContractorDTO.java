package pl.com.chrzanowski.sma.contractor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.json.CountryDeserializer;
import pl.com.chrzanowski.sma.common.json.CountrySerializer;

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
    @JsonDeserialize(using = CountryDeserializer.class)
    @JsonSerialize(using = CountrySerializer.class)
    Country country;
    Boolean customer;
    Boolean supplier;
    Boolean scaffoldingUser;
    Instant createdDatetime;
    Instant lastModifiedDatetime;
    Long createdById;
    String createdByFirstName;
    String createdByLastName;
    Long modifiedById;
    String modifiedByFirstName;
    String modifiedByLastName;
}
