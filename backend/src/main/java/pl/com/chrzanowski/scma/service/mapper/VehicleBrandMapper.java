package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.domain.VehicleBrand;
import pl.com.chrzanowski.scma.service.dto.VehicleBrandDTO;
@Mapper(componentModel = "spring", uses={})
public interface VehicleBrandMapper extends EntityMapper<VehicleBrandDTO, VehicleBrand> {

    default VehicleBrand fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleBrand vehicleBrand = new VehicleBrand();
        vehicleBrand.setId(id);
        return vehicleBrand;
    }
}
