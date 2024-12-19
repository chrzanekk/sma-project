package pl.com.chrzanowski.sma.auth.dto.request;

import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.util.List;

public record UserEditRoleUpdateRequest(Long userId,
                                        List<RoleDTO> roleDTOList) {
}
