package pl.com.chrzanowski.sma.auth.dto.response;

import pl.com.chrzanowski.sma.common.enumeration.ERole;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        List<ERole> roles
) {}
