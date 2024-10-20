package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.domain.Vehicle;
import pl.com.chrzanowski.scma.service.dto.VehicleDTO;

@Mapper(componentModel = "spring", uses = {VehicleModelMapper.class,
        VehicleTypeMapper.class, FuelTypeMapper.class})
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {

    @Mapping(source = "vehicleModel.vehicleBrand.id", target = "brandId")
    @Mapping(source = "vehicleModel.vehicleBrand.name", target = "brandName")
    @Mapping(source = "vehicleModel.id", target = "modelId")
    @Mapping(source = "vehicleModel.name", target = "modelName")
    @Mapping(source = "vehicleType.id", target = "vehicleTypeId")
    @Mapping(source = "vehicleType.name", target = "vehicleTypeName")
    @Mapping(source = "fuelType.id", target = "fuelTypeId")
    @Mapping(source = "fuelType.name", target = "fuelTypeName")
    VehicleDTO toDto(Vehicle vehicle);

    @Mapping(source = "brandId", target = "vehicleModel.vehicleBrand.id")
    @Mapping(source = "brandName", target = "vehicleModel.vehicleBrand.name")
    @Mapping(source = "modelId", target = "vehicleModel.id")
    @Mapping(source = "modelName", target = "vehicleModel.name")
    @Mapping(source = "vehicleTypeId", target = "vehicleType.id")
    @Mapping(source = "vehicleTypeName", target = "vehicleType.name")
    @Mapping(source = "fuelTypeId", target = "fuelType.id")
    @Mapping(source = "fuelTypeName", target = "fuelType.name")
    Vehicle toEntity(VehicleDTO vehicleDTO);

    default Vehicle fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        return vehicle;
    }
}
