package pl.com.chrzanowski.sma.role.mapper;

import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;

@Service
public class MyRoleMapper {

    public Role roleDTOtoRole(RoleDTO roleDTO) {
        if(roleDTO == null) {
            return null;
        } else {
            Role role = new Role();
            role.setName(roleDTO.getName());
            return role;
        }
    }
}
