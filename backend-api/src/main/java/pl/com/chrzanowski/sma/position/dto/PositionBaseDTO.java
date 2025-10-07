package pl.com.chrzanowski.sma.position.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;

@Jacksonized
@SuperBuilder
@EqualsAndHashCode
@ToString
@Getter
public class PositionBaseDTO implements HasId<Long> {
    protected Long id;
    protected String name;
    protected String description;
}
