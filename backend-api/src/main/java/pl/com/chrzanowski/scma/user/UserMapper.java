package pl.com.chrzanowski.scma.user;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.util.mapper.EntityMapper;
import pl.com.chrzanowski.scma.role.RoleMapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
