package pl.com.chrzanowski.scma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.com.chrzanowski.scma.domain.FuelType;
@Repository
public interface FuelTypeRepository extends JpaRepository<FuelType,Long>, JpaSpecificationExecutor<FuelType> {
    void deleteFuelTypeById(Long id);
}
