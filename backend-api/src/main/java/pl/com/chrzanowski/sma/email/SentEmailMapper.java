package pl.com.chrzanowski.sma.email;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.user.UserMapper;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SentEmailMapper extends EntityMapper<SentEmailDTO, SentEmail> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    SentEmailDTO toDto(SentEmail sentEmail);

    @Mapping(source = "userId", target = "user")
    SentEmail toEntity(SentEmailDTO sentEmailDTO);

    default SentEmail fromId(Long id) {
        if (id == null) {
            return null;
        }
        SentEmail sentEmail = new SentEmail();
        sentEmail.setId(id);
        return sentEmail;
    }
}
