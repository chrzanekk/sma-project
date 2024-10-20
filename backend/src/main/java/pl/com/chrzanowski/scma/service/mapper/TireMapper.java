package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.domain.Tire;
import pl.com.chrzanowski.scma.service.dto.TireDTO;

@Mapper(componentModel = "spring", uses = {VehicleMapper.class})
public interface TireMapper extends EntityMapper<TireDTO, Tire> {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    TireDTO toDto(Tire tire);

    @Mapping(source = "vehicleId", target = "vehicle")
    Tire toEntity(TireDTO tireDTO);


    default Tire fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tire tire = new Tire();
        tire.setId(id);
        return tire;
    }
}
