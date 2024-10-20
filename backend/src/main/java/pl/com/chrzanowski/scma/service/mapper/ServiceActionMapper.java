package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.com.chrzanowski.scma.domain.ServiceAction;
import pl.com.chrzanowski.scma.service.dto.ServiceActionDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ServiceActionMapper extends EntityMapper<ServiceActionDTO, ServiceAction> {

    @Mapping(source = "workshop.id", target = "workshopId")
    @Mapping(source = "workshop.name", target = "workshopName")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "vehicle.registrationNumber", target = "vehicleRegistrationNumber")
    ServiceActionDTO toDto(ServiceAction serviceAction);

    @Mapping(source = "workshopId", target = "workshop.id")
    @Mapping(source = "workshopName", target = "workshop.name")
    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(source = "vehicleRegistrationNumber", target = "vehicle.registrationNumber")
    ServiceAction toEntity(ServiceActionDTO serviceActionDTO);

    default ServiceAction fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceAction serviceAction = new ServiceAction();
        serviceAction.setId(id);
        return serviceAction;
    }

}
