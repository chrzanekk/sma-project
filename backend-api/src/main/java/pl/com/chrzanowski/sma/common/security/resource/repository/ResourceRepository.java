// repository/ResourceRepository.java
package pl.com.chrzanowski.sma.common.security.resource.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.sma.common.security.enums.ResourceKey;
import pl.com.chrzanowski.sma.common.security.resource.model.Resource;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Find all active resources with eagerly loaded roles
     */
    @Query("SELECT r FROM Resource r LEFT JOIN FETCH r.allowedRoles WHERE r.isActive = true")
    List<Resource> findAllActiveWithRoles();

    /**
     * Find all resources with eagerly loaded roles (for admin panel)
     */
    @EntityGraph(attributePaths = {"allowedRoles"})
    List<Resource> findAll();
}
