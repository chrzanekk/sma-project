package pl.com.chrzanowski.scma.service.mapper;

import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.domain.Role;
import pl.com.chrzanowski.scma.service.dto.RoleDTO;

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
