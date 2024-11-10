package pl.com.chrzanowski.sma.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.role.dao.RoleDao;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.role.model.Role;
import pl.com.chrzanowski.sma.role.service.RoleService;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.dto.UserDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Mapping(target = "roles", source = "roles")
    UserDTO toDto(User user);

    @Mapping(target = "roles", source = "roles")
    User toEntity(UserDTO userDTO);

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
