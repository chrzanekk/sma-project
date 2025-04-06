package pl.com.chrzanowski.sma.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.user.dto.UserAuditDTO;
import pl.com.chrzanowski.sma.user.model.User;

@Mapper(componentModel = "spring")
public interface UserAuditMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserAuditDTO toDTO(User user);
}
