package pl.com.chrzanowski.sma.role.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.RoleException;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(@Qualifier("jpa") RoleDao roleDao, RoleMapper roleMapper) {
        this.roleDao = roleDao;
        this.roleMapper = roleMapper;
    }

    @Override
    public Set<RoleDTO> findAll() {
        log.info("DAO: Fetching all roles.");
        List<Role> roleList = roleDao.findAll();
        return roleMapper.toDto(Set.copyOf(roleList));
    }

    @Override
    public RoleDTO findByName(ERole name) {
        log.info("Fetching role {}", name);
        Optional<Role> role = roleDao.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("ErrorRole not found: " + ERole.ROLE_ADMIN.getRoleName()));
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Adding new role {} to database", roleDTO.getName());
        if (roleDTO.getName() == null) {
            throw new RoleException("Error RoleName not found");
        }
        RoleDTO roleDTOToSave = roleDTO.toBuilder().createdDatetime(Instant.now()).build();
        return roleMapper.toDto(roleDao.saveRole(roleMapper.toEntity(roleDTOToSave)));
    }
}
