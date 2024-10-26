package pl.com.chrzanowski.sma.contractor.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

@Mapper(componentModel = "spring", uses = {})
public interface ContractorMapper extends EntityMapper<ContractorDTO, Contractor> {

    default Contractor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contractor contractor = new Contractor();
        contractor.setId(id);
        return contractor;
    }
}
