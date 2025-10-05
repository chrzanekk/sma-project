package pl.com.chrzanowski.sma.contract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.contract.dto.ContractDTO;
import pl.com.chrzanowski.sma.contract.model.Contract;

@Mapper(componentModel = "spring", uses = {ContractBaseMapper.class, CompanyBaseMapper.class, ConstructionSiteBaseMapper.class, ContractorBaseMapper.class})
public interface ContractDTOMapper extends EntityMapper<ContractDTO, Contract> {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "signupDate", target = "signupDate")
    @Mapping(source = "realEndDate", target = "realEndDate")
    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(source = "constructionSite", target = "constructionSite")
    @Mapping(source = "contact", target = "contact")
    ContractDTO toDto(Contract contract);


    @Mapping(source = "company", target = "company")
    @Mapping(source = "contractor", target = "contractor")
    @Mapping(source = "constructionSite", target = "constructionSite")
    @Mapping(source = "contact", target = "contact")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    Contract toEntity(ContractDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "constructionSite", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDatetime", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDatetime", ignore = true)
    void updateFromDto(ContractDTO dto, @MappingTarget Contract contract);

}
