package pl.com.chrzanowski.sma.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, CompanyMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "companies", source = "companies")
    UserDTO toDto(User user);

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "companies", source = "companies")
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
