package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ContactBaseMapper {

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    ContactBaseDTO toDto(Contact contact);

    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "modifiedById", target = "modifiedBy.id")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    Contact toEntity(ContactBaseDTO dto);

    default Set<ContactBaseDTO> toDtoSet(Set<Contact> contacts) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream().map(this::toDto).collect(Collectors.toSet());
    }

    default Set<Contact> toEntitySet(Set<ContactBaseDTO> contacts) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
