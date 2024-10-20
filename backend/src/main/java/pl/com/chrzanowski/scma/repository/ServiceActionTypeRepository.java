package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.com.chrzanowski.scma.domain.ServiceActionType;

public interface ServiceActionTypeRepository extends JpaRepository<ServiceActionType, Long>,
        JpaSpecificationExecutor<ServiceActionType> {

    void deleteServiceActionTypeById(Long id);
}
