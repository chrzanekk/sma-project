package pl.com.chrzanowski.scma.auth.response;

import pl.com.chrzanowski.scma.enumeration.ERole;

import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        List<ERole> roles
) {}
