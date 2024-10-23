package pl.com.chrzanowski.sma.role;

import pl.com.chrzanowski.sma.enumeration.ERole;

import java.util.Set;

public interface RoleService {

    Set<RoleDTO> findAll();

    RoleDTO findByName(ERole name);

    RoleDTO saveRole(RoleDTO roleDTO);
}
