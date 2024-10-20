package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.domain.Tire;

@Repository
public interface TireRepository extends JpaRepository<Tire, Long>, JpaSpecificationExecutor<Tire> {

    @Query("select tire from Tire tire where tire.vehicle.id =:vehicleId and tire.tireStatus = 'MOUNTED'")
    Tire findTireByVehicleIdAndStatusMounted(Long vehicleId);

    void deleteTireById(Long id);

}

