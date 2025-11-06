package pl.com.chrzanowski.sma.scaffolding.worktype.dto;

import jakarta.validation.constraints.NotBlank;
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
public class WorkTypeBaseDTO {
    protected Long id;
    @NotBlank
    protected String name;
    protected String description;
}
