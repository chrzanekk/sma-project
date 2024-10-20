package pl.com.chrzanowski.scma.role;

import pl.com.chrzanowski.scma.enumeration.ERole;

import java.util.Set;

public interface RoleService {

    Set<RoleDTO> findAll();

    RoleDTO findByName(ERole name);

    RoleDTO saveRole(RoleDTO roleDTO);
}
