package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

@Mapper(componentModel = "spring")
public interface ContractorBaseMapper extends EntityMapper<ContractorBaseDTO, Contractor> {

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Contractor toEntity(ContractorBaseDTO dto);

    ContractorBaseDTO toDto(Contractor contractor);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ContractorBaseDTO dto, @MappingTarget Contractor entity);
}
