package pl.com.chrzanowski.sma.contracts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contracts.dto.ContractBaseDTO;
import pl.com.chrzanowski.sma.contracts.model.Contract;

@Mapper(componentModel = "spring")
public interface ContractBaseMapper extends EntityMapper<ContractBaseDTO, Contract> {


    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    Contract toEntity(ContractBaseDTO dto);

    ContractBaseDTO toDto(Contract contract);

    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    void updateFromBaseDto(ContractBaseDTO dto, @MappingTarget Contract contract);
}
