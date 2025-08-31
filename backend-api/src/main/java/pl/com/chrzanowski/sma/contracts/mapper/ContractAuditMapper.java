package pl.com.chrzanowski.sma.contracts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contracts.dto.ContractAuditableDTO;
import pl.com.chrzanowski.sma.contracts.model.Contract;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, ContractDTOMapper.class})
public interface ContractAuditMapper extends EntityMapper<ContractAuditableDTO, Contract> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "number", target = "base.number")
    @Mapping(source = "description", target = "base.description")
    @Mapping(source = "value", target = "base.value")
    @Mapping(source = "startDate", target = "base.startDate")
    @Mapping(source = "endDate", target = "base.endDate")
    @Mapping(source = "signupDate", target = "base.signupDate")
    @Mapping(source = "realEndDate", target = "base.realEndDate")
    @Mapping(source = "contractor", target = "base.contractor")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "constructionSite", target = "base.constructionSite")
    ContractAuditableDTO toDto(Contract contract);

}
