package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseDtoMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorHasContactsDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ContactBaseDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractorMapper extends EntityMapper<ContractorDTO, Contractor> {

    @Override
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    ContractorDTO toDto(Contractor contractor);

    @Override
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "modifiedById", target = "modifiedBy.id")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    Contractor toEntity(ContractorDTO contractorDTO);

    @Mapping(source = "contacts", target = "contacts")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    ContractorHasContactsDTO toContractorHasContactsDTO(Contractor contractor);

    default Contractor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contractor contractor = new Contractor();
        contractor.setId(id);
        return contractor;
    }
}
