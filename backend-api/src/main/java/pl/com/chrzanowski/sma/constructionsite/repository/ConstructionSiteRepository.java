package pl.com.chrzanowski.sma.constructionsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import pl.com.chrzanowski.sma.constructionsite.model.ConstructionSite;

public interface ConstructionSiteRepository extends JpaRepository<ConstructionSite, Long>,
        JpaSpecificationExecutor<ConstructionSite>,
        QuerydslPredicateExecutor<ConstructionSite> {
}
