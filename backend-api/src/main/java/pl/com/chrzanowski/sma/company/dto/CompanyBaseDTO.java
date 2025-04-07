package pl.com.chrzanowski.sma.company.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Getter
public class CompanyBaseDTO {
    protected Long id;
    protected String name;
    protected String additionalInfo;
}
