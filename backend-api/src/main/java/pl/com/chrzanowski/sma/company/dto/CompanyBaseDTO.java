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
@ToString
@Getter
public class CompanyBaseDTO {
    protected final Long id;
    protected final String name;
    protected final String additionalInfo;
}
