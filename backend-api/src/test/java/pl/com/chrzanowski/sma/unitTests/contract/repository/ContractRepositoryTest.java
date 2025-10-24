package pl.com.chrzanowski.sma.unitTests.contract.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.com.chrzanowski.sma.AbstractTestContainers;
import pl.com.chrzanowski.sma.contract.model.Contract;
import pl.com.chrzanowski.sma.contract.repository.ContractRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContractRepositoryTest extends AbstractTestContainers {

    @Autowired
    private ContractRepository contractRepository;

    @Test
    void saveAndFindById_Positive() {
        // Given
        Contract contract = Contract.builder()
                .number("C-001")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();

        Contract saved = contractRepository.save(contract);

        // When
        Optional<Contract> found = contractRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("C-001", found.get().getNumber());
    }

    @Test
    void findById_Negative() {
        // When
        Optional<Contract> result = contractRepository.findById(9999L);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Positive() {
        // Given
        Contract contract = Contract.builder()
                .number("C-DELETE")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();

        Contract saved = contractRepository.save(contract);

        // When
        contractRepository.deleteById(saved.getId());
        Optional<Contract> result = contractRepository.findById(saved.getId());

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_Negative() {
        // Given
        Long nonexistentId = 123456L;

        // When
        contractRepository.deleteById(nonexistentId);

        // Then
        Optional<Contract> result = contractRepository.findById(nonexistentId);
        assertTrue(result.isEmpty());
    }
}
