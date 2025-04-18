package pl.com.chrzanowski.sma.constructionsite.dto;

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
@ToString
@Getter
public class ConstructionSiteBaseDTO {
    Long id;
    String name;
    String address;
    @JsonDeserialize(using = CountryDeserializer.class)
    @JsonSerialize(using = CountrySerializer.class)
    Country country;
    String shortName;
    String code;
}
