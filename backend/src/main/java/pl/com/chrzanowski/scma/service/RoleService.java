package pl.com.chrzanowski.scma.service;

import pl.com.chrzanowski.scma.domain.enumeration.ERole;
import pl.com.chrzanowski.scma.service.dto.RoleDTO;

import java.util.Set;

public interface RoleService {

    Set<RoleDTO> findAll();

    RoleDTO findByName(ERole name);

    RoleDTO saveRole(RoleDTO roleDTO);
}
