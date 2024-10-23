package pl.com.chrzanowski.sma.user;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.role.RoleMapper;
import pl.com.chrzanowski.sma.shared.mapper.EntityMapper;

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
