package pl.com.chrzanowski.scma.service.mapper;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.scma.domain.Workshop;
import pl.com.chrzanowski.scma.service.dto.WorkshopDTO;

@Mapper(componentModel = "spring", uses = {ServiceActionTypeMapper.class})
public interface WorkshopMapper extends EntityMapper<WorkshopDTO, Workshop> {

    default Workshop fromId(Long id) {
        if (id == null) {
            return null;
        }
        Workshop workshop = new Workshop();
        workshop.setId(id);
        return workshop;
    }
}
