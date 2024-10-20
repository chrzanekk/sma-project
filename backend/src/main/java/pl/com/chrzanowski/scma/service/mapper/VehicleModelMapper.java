package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.domain.VehicleModel;
import pl.com.chrzanowski.scma.service.dto.VehicleModelDTO;

@Mapper(componentModel = "spring", uses = {VehicleBrandMapper.class})
public interface VehicleModelMapper extends EntityMapper<VehicleModelDTO, VehicleModel> {

    @Mapping(source = "vehicleBrand.id", target = "vehicleBrandId")
    @Mapping(source = "vehicleBrand.name", target = "vehicleBrandName")
    VehicleModelDTO toDto(VehicleModel vehicleModel);

    @Mapping(source = "vehicleBrandId", target = "vehicleBrand.id")
    @Mapping(source = "vehicleBrandName", target = "vehicleBrand.name")
    VehicleModel toEntity(VehicleModelDTO vehicleModelDTO);

    default VehicleModel fromId(Long id) {
        if (id == null) {
            return null;
        }
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setId(id);
        return vehicleModel;
    }
}
