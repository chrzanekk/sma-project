package pl.com.chrzanowski.sma.unitTests.contractor.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.enumeration.Country;
import pl.com.chrzanowski.sma.common.exception.ContractorException;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contractor.dao.ContractorDao;
import pl.com.chrzanowski.sma.contractor.dto.ContractorDTO;
import pl.com.chrzanowski.sma.contractor.mapper.ContractorMapper;
import pl.com.chrzanowski.sma.contractor.model.Contractor;
import pl.com.chrzanowski.sma.contractor.service.ContractorServiceImpl;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContractorServiceImplTest {

    @Mock
    private ContractorDao contractorDao;

    @Mock
    private ContactDao contactDao;

    @Mock
    private ContractorMapper contractorMapper;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ContractorServiceImpl contractorService;

    private ContractorDTO contractorDTO;
    private Contractor contractor;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        contractorDTO = ContractorDTO.builder()
                .id(1L)
                .name("Contractor 1")
                .taxNumber("2323526")
                .street("Street 1")
                .buildingNo("123")
                .apartmentNo("12345")
                .postalCode("2234")
                .city("City 1")
                .country(Country.POLAND)
                .customer(true)
                .supplier(false)
                .scaffoldingUser(true)
                .createdDatetime(Instant.now())
                .contacts(Set.of(ContactBaseDTO.builder().id(42L).build()))
                .build();

        contractor = new Contractor();
        contractor.setId(1L);
        contractor.setName("Contractor 1");
        contractor.setTaxNumber("2323526");
        contractor.setStreet("Street 1");
        contractor.setBuildingNo("123");
        contractor.setApartmentNo("12345");
        contractor.setPostalCode("2234");
        contractor.setCity("City 1");
        contractor.setCountry(Country.POLAND);
        contractor.setCustomer(true);
        contractor.setSupplier(false);
        contractor.setScaffoldingUser(true);

        UserInfoResponse mockUser = new UserInfoResponse(1L, "login", "email@email.com", "test", "user", "position", null, null);
        when(userService.getUserWithAuthorities()).thenReturn(mockUser);

        User user = new User();
        user.setId(mockUser.id());
        user.setLogin(mockUser.login());
        when(entityManager.getReference(User.class, mockUser.id())).thenReturn(user);
    }


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveContractorSuccess() {
        Contact mockContact = new Contact();
        mockContact.setId(42L);

        when(contactDao.findById(42L)).thenReturn(Optional.of(mockContact));
        when(contractorMapper.toEntity(any(ContractorDTO.class))).thenReturn(contractor);
        when(contractorDao.save(any(Contractor.class))).thenReturn(contractor);
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.save(contractorDTO);

        assertNotNull(result);
        assertEquals("Contractor 1", result.getName());

        verify(contractorMapper, times(1)).toEntity(any(ContractorDTO.class));
        verify(contractorDao, times(1)).save(any(Contractor.class));
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testUpdateContractorSuccess() {
        Contact mockContact = new Contact();
        mockContact.setId(42L);

        when(contactDao.findById(42L)).thenReturn(Optional.of(mockContact));
        when(contractorDao.findById(anyLong())).thenReturn(Optional.of(contractor));

        when(contractorMapper.toEntity(any(ContractorDTO.class))).thenReturn(contractor);
        when(contractorDao.save(any(Contractor.class))).thenReturn(contractor);
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.update(contractorDTO);

        assertNotNull(result);
        assertEquals("Contractor 1", result.getName());

        verify(contractorMapper, times(1)).updateContractorFromDto(any(ContractorDTO.class), any(Contractor.class));
        verify(contractorDao, times(1)).save(any(Contractor.class));
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testFindByIdSuccess() {
        when(contractorDao.findById(1L)).thenReturn(Optional.of(contractor));
        when(contractorMapper.toDto(any(Contractor.class))).thenReturn(contractorDTO);

        ContractorDTO result = contractorService.findById(1L);

        assertNotNull(result);
        assertEquals("Contractor 1", result.getName());

        verify(contractorDao, times(1)).findById(1L);
        verify(contractorMapper, times(1)).toDto(any(Contractor.class));
    }

    @Test
    void testFindByIdNotFound() {
        when(contractorDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractorException.class, () -> contractorService.findById(1L));

        verify(contractorDao, times(1)).findById(1L);
    }

    @Test
    void testFindAllSuccess() {
        when(contractorDao.findAll()).thenReturn(Collections.singletonList(contractor));
        when(contractorMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contractorDTO));

        List<ContractorDTO> result = contractorService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contractorDao, times(1)).findAll();
        verify(contractorMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testDeleteSuccess() {
        contractorService.delete(1L);

        verify(contractorDao, times(1)).deleteById(1L);
    }
}
