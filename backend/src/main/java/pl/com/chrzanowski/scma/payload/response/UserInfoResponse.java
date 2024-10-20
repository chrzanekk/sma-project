package pl.com.chrzanowski.scma.payload.response;

import pl.com.chrzanowski.scma.domain.enumeration.ERole;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        List<ERole> roles
) {}
