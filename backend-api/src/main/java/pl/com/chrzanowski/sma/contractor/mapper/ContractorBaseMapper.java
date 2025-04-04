package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.sma.company.mapper.CompanyMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorBaseDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {CompanyMapper.class})
public interface ContractorBaseMapper {

    @Mapping(source = "company.id", target = "companyId")
    ContractorBaseDTO toDto(Contractor contractor);

    @Mapping(source = "companyId",target = "company.id")
    Contractor toEntity(ContractorBaseDTO dto);

    default Set<ContractorBaseDTO> toDtoSet(Set<Contractor> contractors) {
        if (contractors == null) {
            return null;
        }
        return contractors.stream().map(this::toDto).collect(Collectors.toSet());
    }

    default Set<Contractor> toEntitySet(Set<ContractorBaseDTO> contractors) {
        if (contractors == null) {
            return null;
        }
        return contractors.stream().map(this::toEntity).collect(Collectors.toSet());
    }
}
