package pl.com.chrzanowski.scma.role;

import org.springframework.stereotype.Service;

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
