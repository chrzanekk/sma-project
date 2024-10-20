package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.domain.ServiceAction;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceActionRepository extends JpaRepository<ServiceAction, Long>, JpaSpecificationExecutor<ServiceAction> {

    void deleteServiceActionById(Long id);


    List<ServiceAction> findServiceActionByVehicleIdEqualsAndServiceDateGreaterThanEqual(Long vehicleId,
                                                                                   LocalDate serviceDate);

}
