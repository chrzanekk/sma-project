package pl.com.chrzanowski.sma.contact.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactAuditableDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, ContactDTOMapper.class})
public interface ContactAuditMapper extends EntityMapper<ContactAuditableDTO, Contact> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target="base.id")
    @Mapping(source = "firstName", target="base.firstName")
    @Mapping(source = "lastName", target="base.lastName")
    @Mapping(source = "phoneNumber", target="base.phoneNumber")
    @Mapping(source = "email", target="base.email")
    @Mapping(source = "additionalInfo", target="base.additionalInfo")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "contractor", target = "base.contractor")
    ContactAuditableDTO toDto(Contact contact);
}
