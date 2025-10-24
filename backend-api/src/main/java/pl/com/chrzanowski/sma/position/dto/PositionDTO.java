package pl.com.chrzanowski.sma.position.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import pl.com.chrzanowski.sma.common.service.HasId;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.user.dto.UserAuditDTO;
import pl.com.chrzanowski.sma.user.dto.UserDTO;

import java.util.Set;

@Jacksonized
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionDTO extends PositionBaseDTO implements HasId<Long> {
    private CompanyBaseDTO company;
}
