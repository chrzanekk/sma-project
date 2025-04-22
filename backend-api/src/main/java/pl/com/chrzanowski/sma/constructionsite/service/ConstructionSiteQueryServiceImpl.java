package pl.com.chrzanowski.sma.constructionsite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.chrzanowski.sma.constructionsite.dto.ConstructionSiteAuditableDTO;
import pl.com.chrzanowski.sma.constructionsite.service.filter.ConstructionSiteFilter;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConstructionSiteQueryServiceImpl implements ConstructionSiteQueryService {


    @Override
    @Transactional
    public List<ConstructionSiteAuditableDTO> findByFilter(ConstructionSiteFilter filter) {
        return List.of();
    }

    @Override
    @Transactional
    public Page<ConstructionSiteAuditableDTO> findByFilter(ConstructionSiteFilter filter, Pageable pageable) {
        return null;
    }
}
