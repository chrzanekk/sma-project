package pl.com.chrzanowski.sma.scaffolding.position.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.scaffolding.position.dto.ScaffoldingLogPositionAuditableDTO;
import pl.com.chrzanowski.sma.scaffolding.position.service.filter.ScaffoldingLogPositionFilter;

import java.util.List;

@Service
@Transactional
public class ScaffoldingLogPositionQueryServiceImpl implements ScaffoldingLogPositionQueryService {

    @Override
    public List<ScaffoldingLogPositionAuditableDTO> findByFilter(ScaffoldingLogPositionFilter filter) {
        return List.of();
    }

    @Override
    public Page<ScaffoldingLogPositionAuditableDTO> findByFilter(ScaffoldingLogPositionFilter filter, Pageable pageable) {
        return null;
    }
}
