package pl.com.chrzanowski.sma.auth.dto.response;

import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String login,
        String email,
        String firstName,
        String lastName,
        String position,
        List<String> roles,
        List<CompanyBaseDTO> companies
) {
}
