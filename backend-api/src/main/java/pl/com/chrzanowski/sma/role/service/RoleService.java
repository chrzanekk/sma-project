package pl.com.chrzanowski.sma.role.service;

import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleDTO saveRole(RoleDTO role);

    RoleDTO findByName(String name);

    List<RoleDTO> findAll();

    Set<RoleDTO> findAllByListOfNames(List<String> names);

    boolean deleteById(Long id);
}
