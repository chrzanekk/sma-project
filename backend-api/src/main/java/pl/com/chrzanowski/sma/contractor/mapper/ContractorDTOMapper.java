package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contract.mapper.ContractBaseMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorUpdateDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

@Mapper(componentModel = "spring", uses = {ContractorBaseMapper.class, CompanyBaseMapper.class, ContactBaseMapper.class, ContractBaseMapper.class})
public interface ContractorDTOMapper extends EntityMapper<ContractorDTO, Contractor> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Contractor toEntity(ContractorDTO dto);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "contacts", target = "contacts")
    @Mapping(source = "contracts", target = "contracts")
    ContractorDTO toDto(Contractor contractor);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromDto(ContractorDTO contractorDTO, @MappingTarget Contractor contractor);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "contacts", source = "contacts",ignore = true)
    @Mapping(target = "contracts", source = "contracts",ignore = true)
    @Mapping(target = "company", source = "company",ignore = true)
    void updateFromUpdateDto(ContractorUpdateDTO contractorUpdateDTO, @MappingTarget Contractor contractor);
}
