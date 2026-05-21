package pl.com.chrzanowski.sma.scaffolding.log.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.company.mapper.CompanyBaseMapper;
import pl.com.chrzanowski.sma.constructionsite.mapper.ConstructionSiteBaseMapper;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorBaseMapper;
import pl.com.chrzanowski.sma.scaffolding.log.dto.ScaffoldingLogAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.log.model.ScaffoldingLog;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionBaseDTO;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;
import pl.com.chrzanowski.sma.user.mapper.UserAuditMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        UserAuditMapper.class,
        ScaffoldingLogDTOMapper.class,
        CompanyBaseMapper.class,
        ContractorBaseMapper.class,
        ConstructionSiteBaseMapper.class})
public interface ScaffoldingLogAuditMapper extends EntityMapper<ScaffoldingLogAuditableDTO, ScaffoldingLog> {


    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdDatetime", target = "createdDatetime")
    @Mapping(source = "lastModifiedDatetime", target = "lastModifiedDatetime")
    @Mapping(source = "id", target = "base.id")
    @Mapping(source = "name", target = "base.name")
    @Mapping(source = "additionalInfo", target = "base.additionalInfo")
    @Mapping(source = "company", target = "base.company")
    @Mapping(source = "constructionSite", target = "base.constructionSite")
    @Mapping(source = "contractor", target = "base.contractor")
    @Mapping(source = "positions", target = "base.positions", qualifiedByName = "positionsMapping")
    ScaffoldingLogAuditableDTO toDto(ScaffoldingLog scaffoldingLog);

    @Named("positionsMapping")
    default Set<ScaffoldingLogPositionBaseDTO> mapPositions(Set<ScaffoldingLogPosition> positions) {
        // Użyj prostego mappera bez zagnieżdżonych kolekcji
        return positions == null ? null : positions.stream()
                .map(this::positionToSummaryDTO)
                .collect(Collectors.toSet());
    }

    // Specjalna metoda mapująca pozycję TYLKO na potrzeby listy w Logu
    // Kluczowe jest IGNOROWANIE pól powodujących pętle
    @Mapping(target = "scaffoldingLog", ignore = true)      // PRZERYWA PĘTLĘ GŁÓWNĄ (Log -> Position -> Log)
    @Mapping(target = "parentPosition", ignore = true)      // Przerywa pętlę rodzica
    @Mapping(target = "childPositions", ignore = true)      // Przerywa pętlę dzieci
    @Mapping(target = "workingTimes", ignore = true)        // Opcjonalnie: jeśli nie potrzebujesz czasów pracy w widoku listy logów
    @Mapping(target = "dimensions", ignore = true)          // Opcjonalnie: jeśli nie potrzebujesz wymiarów w widoku listy
    // Upewnij się, że mapujesz pozostałe wymagane pola (np. company, contractor) jeśli są w BaseDTO
    ScaffoldingLogPositionBaseDTO positionToSummaryDTO(ScaffoldingLogPosition position);
}
