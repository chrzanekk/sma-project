package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactBaseDtoMapper {

    ContactDTO toBaseContactDTO(Contact contact);
}
