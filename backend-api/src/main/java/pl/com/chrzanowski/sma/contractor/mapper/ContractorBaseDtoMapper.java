package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractorBaseDtoMapper {

    ContractorDTO toBaseContractorDTO(Contractor contractor);
}
