package pl.com.chrzanowski.sma.role;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.shared.mapper.EntityMapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    default Role fromId(Long id) {
        if (id == null) {
            return null;
        }
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
