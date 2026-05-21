package pl.com.chrzanowski.sma.scaffolding.log.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class ScaffoldingLogBaseDTO {
    protected Long id;
    protected String name;
    protected String additionalInfo;
}
