package pl.com.chrzanowski.sma.user.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.position.mapper.PositionBaseMapper;
import pl.com.chrzanowski.sma.role.mapper.RoleMapper;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;

@Mapper(componentModel = "spring",uses = {CompanyBaseMapper.class, RoleMapper.class, PositionBaseMapper.class})
public interface UserDTOMapper extends EntityMapper<UserDTO, User> {

}
