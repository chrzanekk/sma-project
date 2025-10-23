package pl.com.chrzanowski.sma.auth.dto.response;

import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;
import pl.com.chrzanowski.sma.position.dto.PositionDTO;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String login,
        String email,
        String firstName,
        String lastName,
        PositionBaseDTO position,
        List<String> roles,
        List<CompanyBaseDTO> companies
) {
}
