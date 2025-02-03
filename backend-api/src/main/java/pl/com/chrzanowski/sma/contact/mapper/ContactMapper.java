package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactHasContractorsDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseDtoMapper;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ContractorBaseDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {

    @Override
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    ContactDTO toDto(Contact contact);

    @Override
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "modifiedById", target = "modifiedBy.id")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    Contact toEntity(ContactDTO contactDTO);

    @Mapping(source = "contractors", target = "contractors")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    ContactHasContractorsDTO toContactHasContractorsDTO(Contact contact);

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
