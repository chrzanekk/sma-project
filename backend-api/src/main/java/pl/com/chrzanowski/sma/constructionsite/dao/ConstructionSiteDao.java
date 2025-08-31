package pl.com.chrzanowski.sma.constructionsite.dao;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;

import java.util.List;
import java.util.Optional;

public interface ConstructionSiteDao {

    ConstructionSite save(ConstructionSite constructionSite);

    Optional<ConstructionSite> findById(Long id);

    Page<ConstructionSite> findAll(BooleanBuilder specification, Pageable pageable);

    List<ConstructionSite> findAll(BooleanBuilder specification);

    void deleteById(Long id);
}
