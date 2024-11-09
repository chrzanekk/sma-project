package pl.com.chrzanowski.sma.role.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<RoleDTO> findAll() {
        log.debug("DAO: Fetching all roles.");
        List<Role> roleList = roleDao.findAll();
        return roleMapper.toDtoList(roleList);
    }

    @Override
    public RoleDTO findByName(String name) {
        log.debug("Fetching role {}", name);
        Optional<Role> role = roleDao.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("ErrorRole not found: " + name));
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.debug("Adding new role {} to database", roleDTO.getName());
        if (roleDTO.getName() == null) {
            throw new RoleException("Error RoleName not found");
        }
        RoleDTO roleDTOToSave = roleDTO.toBuilder().createdDatetime(Instant.now()).build();
        return roleMapper.toDto(roleDao.saveRole(roleMapper.toEntity(roleDTOToSave)));
    }

    @Override
    public boolean deleteById(Long id) {
        log.debug("Deleting role {}", id);
        Optional<Role> existingRole = roleDao.findById(id);
        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            if (isRoleIsBase(role)) {
                throw new RoleException(String.format("Cannot delete role %s", role.getName()));
            } else {
                roleDao.deleteById(id);
                return true;
            }
        } else {
            throw new RoleException("Error Role not found");
        }
    }

    private boolean isRoleIsBase(Role role) {
        return role.getName().equals(ERole.ROLE_USER.getRoleName())
                || role.getName().equals(ERole.ROLE_ADMIN.getRoleName())
                || role.getName().equals(ERole.ROLE_MODERATOR.getRoleName());
    }
}
