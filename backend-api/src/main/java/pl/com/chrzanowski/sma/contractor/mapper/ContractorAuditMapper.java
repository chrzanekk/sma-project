package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorAuditableDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

@Mapper(componentModel = "spring", uses = {UserAuditMapper.class, ContractorDTOMapper.class})
public interface ContractorAuditMapper extends EntityMapper<ContractorAuditableDTO, Contractor> {

    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "taxNumber", target = "base.taxNumber")
    @Mapping(source = "street", target = "base.street")
    @Mapping(source = "buildingNo", target = "base.buildingNo")
    @Mapping(source = "apartmentNo", target = "base.apartmentNo")
    @Mapping(source = "postalCode", target = "base.postalCode")
    @Mapping(source = "city", target = "base.city")
    @Mapping(source = "country", target = "base.country")
    @Mapping(source = "customer", target = "base.customer")
    @Mapping(source = "supplier", target = "base.supplier")
    @Mapping(source = "scaffoldingUser", target = "base.scaffoldingUser")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "contacts", target = "base.contacts")
    ContractorAuditableDTO toDto(Contractor contractor);
}
