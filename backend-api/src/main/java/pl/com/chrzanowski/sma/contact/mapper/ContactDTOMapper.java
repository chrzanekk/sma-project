package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;

@Mapper(componentModel = "spring", uses = {ContactBaseMapper.class, CompanyBaseMapper.class, ContractorBaseMapper.class})
public interface ContactDTOMapper extends EntityMapper<ContactDTO, Contact> {

    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(source = "id", target="id")
    @Mapping(source = "firstName", target="firstName")
    @Mapping(source = "lastName", target="lastName")
    @Mapping(source = "phoneNumber", target="phoneNumber")
    @Mapping(source = "email", target="email")
    @Mapping(source = "additionalInfo", target="additionalInfo")
    ContactDTO toDto(Contact contact);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Contact toEntity(ContactDTO contactDTO);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(ContactDTO contactDTO, @MappingTarget Contact contact);
}
