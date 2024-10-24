package pl.com.chrzanowski.sma.workshop;

import org.mapstruct.Mapper;
import pl.com.chrzanowski.sma.common.mapper.EntityMapper;

@Mapper(componentModel = "spring", uses = {})
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
