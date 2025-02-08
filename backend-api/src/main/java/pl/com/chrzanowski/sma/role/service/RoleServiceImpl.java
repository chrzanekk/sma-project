package pl.com.chrzanowski.sma.role.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.common.enumeration.ERole;
import pl.com.chrzanowski.sma.common.exception.RoleException;
import pl.com.chrzanowski.sma.common.exception.error.RoleErrorCode;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.role.model.Role;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleDao roleDao, RoleMapper roleMapper) {
        this.roleDao = roleDao;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional
    public List<RoleDTO> findAll() {
        log.debug("DAO: Fetching all roles.");
        List<Role> roleList = roleDao.findAll();
        return roleMapper.toDtoList(roleList);
    }

    @Override
    @Transactional
    public RoleDTO findByName(String name) {
        log.debug("Fetching role {}", name);
        Optional<Role> role = roleDao.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("Error: Role not found: " + name, Map.of("roleName", name)));
    }

    @Override
    @Transactional
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.debug("Adding new role {} to database", roleDTO.getName());
        if (roleDTO.getName() == null) {
            throw new RoleException("Error RoleName not found");
        }
        if (!roleDTO.getName().startsWith("ROLE_")) {
            String roleName = "ROLE_" + roleDTO.getName();
            roleDTO = roleDTO.toBuilder().name(roleName).build();
        }
        roleDTO = roleDTO.toBuilder().name(roleDTO.getName().toUpperCase()).build();
        RoleDTO roleDTOToSave = roleDTO.toBuilder().createdDatetime(Instant.now()).build();
        return roleMapper.toDto(roleDao.saveRole(roleMapper.toEntity(roleDTOToSave)));
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Deleting role {}", id);
        Optional<Role> existingRole = roleDao.findById(id);
        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            if (isRoleIsBase(role)) {
                throw new RoleException(RoleErrorCode.ROLE_CANNOT_BE_DELETED, String.format("Cannot delete role %s", role.getName()), Map.of("roleName", role.getName()));
            } else {
                roleDao.deleteById(id);
                return true;
            }
        } else {
            throw new RoleException("Error Role not found");
        }
    }

    @Override
    public Set<RoleDTO> findAllByListOfNames(List<String> names) {
        log.debug("Fetching all roles with names {}", names);
        return names.stream().map(this::findByName).collect(Collectors.toSet());
    }

    private boolean isRoleIsBase(Role role) {
        return role.getName().equals(ERole.ROLE_USER.getRoleName())
                || role.getName().equals(ERole.ROLE_ADMIN.getRoleName())
                || role.getName().equals(ERole.ROLE_MODERATOR.getRoleName());
    }
}
