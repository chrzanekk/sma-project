package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.domain.VehicleType;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> , JpaSpecificationExecutor<VehicleType> {
    void deleteVehicleTypeById(Long id);
}
