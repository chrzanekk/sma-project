package pl.com.chrzanowski.scma.auth.resettoken;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.user.UserMapper;
import pl.com.chrzanowski.scma.util.mapper.EntityMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PasswordResetTokenMapper extends EntityMapper<PasswordResetTokenDTO, PasswordResetToken> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "userName")
    PasswordResetTokenDTO toDto(PasswordResetToken passwordResetToken);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "userName", target = "user.username")
    PasswordResetToken toEntity(PasswordResetTokenDTO passwordResetTokenDTO);

    default PasswordResetToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setId(id);
        return passwordResetToken;
    }
}
