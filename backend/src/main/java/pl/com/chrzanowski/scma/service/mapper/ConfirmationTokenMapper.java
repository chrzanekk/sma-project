package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.domain.ConfirmationToken;
import pl.com.chrzanowski.scma.service.dto.ConfirmationTokenDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ConfirmationTokenMapper extends EntityMapper<ConfirmationTokenDTO, ConfirmationToken> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "userName")
    ConfirmationTokenDTO toDto(ConfirmationToken confirmationToken);
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "userName", target = "user.username")
    ConfirmationToken toEntity(ConfirmationTokenDTO confirmationTokenDTO);

    default ConfirmationToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setId(id);
        return confirmationToken;
    }
}
