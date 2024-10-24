package pl.com.chrzanowski.sma.role.mapper;

import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.model.Role;

@Component
public class MyRoleMapper {

    public Role roleDTOtoRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        } else {
            Role role = new Role();
            role.setName(roleDTO.getName());
            return role;
        }
    }
}
