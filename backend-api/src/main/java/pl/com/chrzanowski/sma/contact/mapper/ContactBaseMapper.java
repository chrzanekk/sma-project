package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactBaseMapper extends EntityMapper<ContactBaseDTO, Contact> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Contact toEntity(ContactBaseDTO dto);

    ContactBaseDTO toDto(Contact contact);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ContactBaseDTO contactDTO, @MappingTarget Contact contact);

}
