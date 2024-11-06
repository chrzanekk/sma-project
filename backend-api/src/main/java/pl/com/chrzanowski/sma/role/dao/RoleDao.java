package pl.com.chrzanowski.sma.role.dao;

import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleDao {

    List<Role> findAll();

    Optional<Role> findByName(String name);

    Role saveRole(Role role);
}
