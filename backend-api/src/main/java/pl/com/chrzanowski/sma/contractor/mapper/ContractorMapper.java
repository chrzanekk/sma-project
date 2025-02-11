package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.user.mapper.UserMapper;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, ContactBaseMapper.class},
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
    @Mapping(target = "contacts", expression = "java(contactBaseMapper.toDtoSet(contractor.getContacts()))")
    public abstract ContractorDTO toDto(Contractor contractor);

    @Override
    @Mapping(source = "createdById", target = "createdBy.id")
    @Mapping(source = "modifiedById", target = "modifiedBy.id")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(target = "contacts", expression = "java(contactBaseMapper.toEntitySet(contractorDTO.getContacts()))")
    public abstract Contractor toEntity(ContractorDTO contractorDTO);

    // Mapowanie do wersji bazowej
    public abstract ContractorBaseDTO toBaseDto(Contractor contractor);

}
