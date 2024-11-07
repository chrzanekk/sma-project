package pl.com.chrzanowski.sma.role.service;

import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    List<RoleDTO> findAll();

    RoleDTO findByName(String name);

    RoleDTO saveRole(RoleDTO role);

    boolean deleteById(Long id);
}
