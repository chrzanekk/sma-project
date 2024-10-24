package pl.com.chrzanowski.sma.role.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class RoleJPADaoImpl implements RoleDao {

    private final Logger log = LoggerFactory.getLogger(RoleJPADaoImpl.class);

    private final RoleRepository roleRepository;

    public RoleJPADaoImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        log.debug("JPA DAO: Fetching all roles.");
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findByName(ERole name) {
        log.debug("JPA DAO: Fetching role {}", name);
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        log.debug("JPA DAO: Adding new role {} to database", role.getName());
        return roleRepository.save(role);
    }
}
