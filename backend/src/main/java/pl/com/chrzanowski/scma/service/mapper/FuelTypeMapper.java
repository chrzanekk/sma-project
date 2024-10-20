package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.domain.FuelType;
import pl.com.chrzanowski.scma.service.dto.FuelTypeDTO;

@Mapper(componentModel = "spring", uses = {})
public interface FuelTypeMapper extends EntityMapper<FuelTypeDTO, FuelType> {

    default FuelType fromId(Long id) {
        if (id == null) {
            return null;
        }
        FuelType fuelType = new FuelType();
        fuelType.setId(id);
        return fuelType;
    }
}
