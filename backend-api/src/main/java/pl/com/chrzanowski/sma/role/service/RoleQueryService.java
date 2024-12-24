package pl.com.chrzanowski.sma.role.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.role.service.filter.RoleFilter;

import java.util.List;

public interface RoleQueryService {

    List<RoleDTO> findByFilter(RoleFilter filter);

    Page<RoleDTO> findByFilter(RoleFilter filter, Pageable pageable);
}
