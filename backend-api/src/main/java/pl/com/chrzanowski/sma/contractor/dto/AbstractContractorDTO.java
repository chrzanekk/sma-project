package pl.com.chrzanowski.sma.contractor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.json.CountryDeserializer;
import pl.com.chrzanowski.sma.common.json.CountrySerializer;

import java.time.Instant;

@Getter
@SuperBuilder
@NoArgsConstructor(force = true)
public abstract class AbstractContractorDTO {
    protected final Long id;
    protected final String name;
    protected final String taxNumber;
    protected final String street;
    protected final String buildingNo;
    protected final String apartmentNo;
    protected final String postalCode;
    protected final String city;
    @JsonDeserialize(using = CountryDeserializer.class)
    @JsonSerialize(using = CountrySerializer.class)
    protected final Country country;
    protected final Boolean customer;
    protected final Boolean supplier;
    protected final Boolean scaffoldingUser;
    protected final Instant createdDatetime;
    protected final Instant lastModifiedDatetime;
    protected final Long createdById;
    protected final String createdByFirstName;
    protected final String createdByLastName;
    protected final Long modifiedById;
    protected final String modifiedByFirstName;
    protected final String modifiedByLastName;
}
