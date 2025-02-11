package pl.com.chrzanowski.sma.usertoken.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.usertoken.model.UserToken;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserTokenMapper extends EntityMapper<UserTokenDTO, UserToken> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "createDate", target = "createDate")
    @Mapping(source = "expireDate", target = "expireDate")
    @Mapping(source = "useDate", target = "useDate")
    @Mapping(source = "tokenType", target = "tokenType")
    UserTokenDTO toDto(UserToken userToken);


    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "login", target = "user.login")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "createDate", target = "createDate")
    @Mapping(source = "expireDate", target = "expireDate")
    @Mapping(source = "useDate", target = "useDate")
    @Mapping(source = "tokenType", target = "tokenType")
    UserToken toEntity(UserTokenDTO userTokenDTO);

    default UserToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserToken userToken = new UserToken();
        userToken.setId(id);
        return userToken;
    }
}
