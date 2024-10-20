package pl.com.chrzanowski.scma.role;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.com.chrzanowski.scma.enumeration.ERole;
import pl.com.chrzanowski.scma.exception.RoleException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Set<RoleDTO> findAll() {
        log.info("Fetching all roles.");
        List<Role> roleList = roleRepository.findAll();
        return roleMapper.toDto(Set.copyOf(roleList));
    }

    @Override
    public RoleDTO findByName(ERole name) {
        log.info("Fetching role {}", name);
        Optional<Role> role = roleRepository.findByName(name);
        return role.map(roleMapper::toDto)
                .orElseThrow(() -> new RoleException("ErrorRole not found: " + ERole.ROLE_ADMIN.getRoleName()));
    }

    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Adding new role {} to database", roleDTO.getName());
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }
}
