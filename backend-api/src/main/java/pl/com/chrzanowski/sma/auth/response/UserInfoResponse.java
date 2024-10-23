package pl.com.chrzanowski.sma.auth.response;

import pl.com.chrzanowski.sma.enumeration.ERole;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        List<ERole> roles
) {}
