package pl.com.chrzanowski.sma.role.service;

import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.util.Set;

public interface RoleService {

    Set<RoleDTO> findAll();

    RoleDTO findByName(String name);

    RoleDTO saveRole(RoleDTO role);

    boolean deleteById(Long id);
}
