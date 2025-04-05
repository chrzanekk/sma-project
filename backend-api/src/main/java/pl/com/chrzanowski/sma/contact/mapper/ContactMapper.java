package pl.com.chrzanowski.sma.contact.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring",
        uses = {CompanyMapper.class, UserAuditMapper.class, ContractorBaseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ContactMapper implements EntityMapper<ContactDTO, Contact> {


    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    public abstract ContactDTO toDto(Contact contact);

    @Mapping(target = "contractor", source = "contractor")
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "company", source = "company")
    public abstract void updateContactFromDto(ContactDTO contactDTO, @MappingTarget Contact contact);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "company", source = "company")
    public abstract Contact toEntity(ContactDTO contactDTO);
}
