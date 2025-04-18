package pl.com.chrzanowski.sma.constructionsite.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteDTO;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractor;
import pl.com.chrzanowski.sma.constructionsitecontractor.model.ConstructionSiteContractorId;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ConstructionSiteBaseMapper.class, CompanyBaseMapper.class, ContractorBaseMapper.class})
public interface ConstructionSiteDTOMapper extends EntityMapper<ConstructionSiteDTO, ConstructionSite> {

    ContractorBaseMapper CONTRACTOR_MAPPER =
            Mappers.getMapper(ContractorBaseMapper.class);

    @Mapping(source = "company", target = "company")
    @Mapping(source = "siteContractors", target = "contractors")
    ConstructionSiteDTO toDto(ConstructionSite constructionSite);

    default List<ContractorBaseDTO>
    mapSiteContractorsToDtos(Set<ConstructionSiteContractor> siteContractors) {

        if (siteContractors == null) {
            return null;
        }
        return siteContractors.stream()
                .map(csc -> CONTRACTOR_MAPPER.toDto(csc.getContractor()))
                .collect(Collectors.toList());
    }

    @Mapping(target = "siteContractors", ignore = true)
    @Mapping(source = "company", target = "company")
    ConstructionSite toEntity(ConstructionSiteDTO constructionSiteDTO);

    @Mapping(source = "company", target = "company")
    @Mapping(target = "siteContractors", ignore = true)
    void updateFromDto(ConstructionSiteDTO constructionSiteDTO, @MappingTarget ConstructionSite constructionSite);

    @AfterMapping
    default void enrichSiteContractors(
            ConstructionSiteDTO dto,
            @MappingTarget ConstructionSite entity
    ) {
        if (dto.getContractors() == null) {
            return;
        }

        Set<ConstructionSiteContractor> constructionSiteContractors = dto.getContractors().stream()
                .map(cb -> {
                    Contractor contractor = CONTRACTOR_MAPPER.toEntity(cb);
                    ConstructionSiteContractor csc = new ConstructionSiteContractor();
                    csc.setConstructionSite(entity);
                    csc.setContractor(contractor);
                    csc.setId(new ConstructionSiteContractorId(
                            entity.getId(),
                            contractor.getId()
                    ));
                    return csc;
                })
                .collect(Collectors.toSet());

        entity.getSiteContractors().clear();
        entity.getSiteContractors().addAll(constructionSiteContractors);
    }
}
