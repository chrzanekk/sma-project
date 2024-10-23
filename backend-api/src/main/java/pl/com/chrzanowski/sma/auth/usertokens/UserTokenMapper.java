package pl.com.chrzanowski.sma.auth.usertokens;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.shared.mapper.EntityMapper;
import pl.com.chrzanowski.sma.user.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserTokenMapper extends EntityMapper<UserTokenDTO, UserToken> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "createDate", target = "createDate")
    @Mapping(source = "expireDate", target = "expireDate")
    @Mapping(source = "useDate", target = "useDate")
    @Mapping(source = "tokenType", target = "tokenType")
    UserTokenDTO toDto(UserToken userToken);


    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "userName", target = "user.username")
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
