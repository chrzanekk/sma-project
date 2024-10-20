package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.domain.VehicleType;
import pl.com.chrzanowski.scma.service.dto.VehicleTypeDTO;

@Mapper(componentModel = "spring", uses = {})
public interface VehicleTypeMapper extends EntityMapper<VehicleTypeDTO, VehicleType> {

    default VehicleType fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(id);
        return vehicleType;
    }
}
