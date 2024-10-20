package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.domain.ServiceActionType;
import pl.com.chrzanowski.scma.service.dto.ServiceActionTypeDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ServiceActionTypeMapper extends EntityMapper<ServiceActionTypeDTO, ServiceActionType> {

    default ServiceActionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceActionType serviceActionType = new ServiceActionType();
        serviceActionType.setId(id);
        return serviceActionType;
    }

}
