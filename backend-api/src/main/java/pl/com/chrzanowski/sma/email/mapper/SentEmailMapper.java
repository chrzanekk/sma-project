package pl.com.chrzanowski.sma.email.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.email.dto.SentEmailDTO;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SentEmailMapper extends EntityMapper<SentEmailDTO, SendEmail> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    SentEmailDTO toDto(SendEmail sendEmail);

    @Mapping(source = "userId", target = "user")
    SendEmail toEntity(SentEmailDTO sentEmailDTO);

    default SendEmail fromId(Long id) {
        if (id == null) {
            return null;
        }
        SendEmail sendEmail = new SendEmail();
        sendEmail.setId(id);
        return sendEmail;
    }
}
