package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ContactBaseMapper {

    ContactBaseDTO toDto(Contact contact);

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
