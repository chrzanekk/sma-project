package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, UserMapper.class})
public interface ContactBaseMapper {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "company", target = "company")
    ContactBaseDTO toDto(Contact contact);

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "company", target = "company")
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

    default List<ContactBaseDTO> toDtoList(List<Contact> contacts) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream().map(this::toDto).toList();
    }

    default List<Contact> toEntityList(List<ContactBaseDTO> contacts) {
        if (contacts == null) {
            return null;
        }
        return contacts.stream().map(this::toEntity).toList();
    }
}
