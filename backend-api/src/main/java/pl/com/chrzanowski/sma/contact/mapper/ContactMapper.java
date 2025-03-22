package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ContractorBaseMapper.class})
public abstract class ContactMapper implements EntityMapper<ContactDTO, Contact> {

    @Autowired
    protected ContractorBaseMapper contractorBaseMapper;

    // Mapowanie z encji do pełnego DTO – pole contractors mapujemy przy użyciu metody
    // toDtoSet z ContractorBaseMapper.
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    @Mapping(target = "contractors", expression = "java(contractorBaseMapper.toDtoSet(contact.getContractors()))")
    public abstract ContactDTO toDto(Contact contact);

    @Mapping(target = "contractors", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract void updateContactFromDto(ContactDTO contactDTO, @MappingTarget Contact contact);

    @Mapping(target = "contractors", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    public abstract Contact toEntity(ContactDTO contactDTO);

    // Mapowanie do wersji bazowej
    public abstract ContactBaseDTO toBaseDto(Contact contact);
}
