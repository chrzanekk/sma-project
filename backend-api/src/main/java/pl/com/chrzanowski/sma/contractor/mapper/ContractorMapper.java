package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ContactBaseMapper.class, CompanyMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ContractorMapper implements EntityMapper<ContractorDTO, Contractor> {

    @Autowired
    protected ContactBaseMapper contactBaseMapper;

    @Override
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "modifiedBy.id", target = "modifiedById")
    @Mapping(source = "createdBy.firstName", target = "createdByFirstName")
    @Mapping(source = "createdBy.lastName", target = "createdByLastName")
    @Mapping(source = "modifiedBy.firstName", target = "modifiedByFirstName")
    @Mapping(source = "modifiedBy.lastName", target = "modifiedByLastName")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(target = "contacts", expression = "java(contactBaseMapper.toDtoSet(contractor.getContacts()))")
    public abstract ContractorDTO toDto(Contractor contractor);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "contacts", expression = "java(contactBaseMapper.toEntitySet(contractorDTO.getContacts()))")
    public abstract void updateContractorFromDto(ContractorDTO contractorDTO, @MappingTarget Contractor contractor);

    @Override
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "company.id", source = "companyId")
    @Mapping(target = "contacts", expression = "java(contactBaseMapper.toEntitySet(contractorDTO.getContacts()))")
    public abstract Contractor toEntity(ContractorDTO contractorDTO);

    // Mapowanie do wersji bazowej
    public abstract ContractorBaseDTO toBaseDto(Contractor contractor);

}
