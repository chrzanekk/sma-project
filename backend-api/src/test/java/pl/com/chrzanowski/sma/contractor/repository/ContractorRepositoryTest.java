package pl.com.chrzanowski.sma.contractor.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.contractor.model.Contractor;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ContractorRepositoryTest extends AbstractTestContainers {
    @Autowired
    private ContractorRepository contractorRepository;

    @Test
    void deleteById_Positive() {
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").taxNumber("1234567890").build();
        Contractor savedContractor = contractorRepository.save(contractor);

        // When
        contractorRepository.deleteById(savedContractor.getId());
        var deletedContractor = contractorRepository.findById(savedContractor.getId());

        // Then
        assertTrue(deletedContractor.isEmpty());
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long nonexistentId = 999L;

        // When
        contractorRepository.deleteById(nonexistentId);
        var deletedContractor = contractorRepository.findById(nonexistentId);

        // Then
        assertTrue(deletedContractor.isEmpty());
    }


    @Test
    void findByName_Positive() {
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").taxNumber("1234567890").build();
        contractorRepository.save(contractor);

        // When
        var foundContractor = contractorRepository.findByName("Test Contractor");

        // Then
        assertTrue(foundContractor.isPresent());
        assertEquals("Test Contractor", foundContractor.get().getName());
    }

    @Test
    void findByName_Negative() {
        // When
        var foundContractor = contractorRepository.findByName("Nonexistent Contractor");

        // Then
        assertTrue(foundContractor.isEmpty());
    }


    @Test
    void existsByName_Positive() {
        // Given
        Contractor contractor = Contractor.builder().name("Test Contractor").taxNumber("1234567890").build();
        contractorRepository.save(contractor);

        // When
        boolean exists = contractorRepository.existsByName("Test Contractor");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByName_Negative() {
        // When
        boolean exists = contractorRepository.existsByName("Nonexistent Contractor");

        // Then
        assertFalse(exists);
    }

}