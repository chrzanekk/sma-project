package pl.com.chrzanowski.scma.role;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.util.mapper.EntityMapper;

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
