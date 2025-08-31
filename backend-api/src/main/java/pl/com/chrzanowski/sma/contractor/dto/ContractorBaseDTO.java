package pl.com.chrzanowski.sma.contractor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.json.CountryDeserializer;
import pl.com.chrzanowski.sma.common.json.CountrySerializer;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class ContractorBaseDTO {
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
}
