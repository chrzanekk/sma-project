package pl.com.chrzanowski.sma.unitTests.contact.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.common.exception.ContactException;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactBaseDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactBaseMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactServiceImpl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactServiceImplTest {

    @Mock
    private ContactDao contactDao;

    @Mock
    private ContactBaseMapper contactBaseMapper;

    @InjectMocks
    private ContactServiceImpl contactService;

    private ContactBaseDTO contactDTO;
    private Contact contact;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        contactDTO = ContactBaseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("555-555-555")
                .email("john@doe.com")
                .createdDatetime(Instant.now())
                .build();

        contact = new Contact();
        contact.setId(1L);
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john@doe.com");
        contact.setPhoneNumber("555-555-555");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveContact() {
        when(contactBaseMapper.toEntity(any(ContactBaseDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenReturn(contact);
        when(contactBaseMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactBaseDTO result = contactService.save(contactDTO);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("555-555-555", result.getPhoneNumber());
        assertEquals("john@doe.com", result.getEmail());

        verify(contactDao, times(1)).save(any(Contact.class));
        verify(contactBaseMapper, times(1)).toDto(any(Contact.class));
    }

    @Test
    void testSaveContactFailure() {
        when(contactBaseMapper.toEntity(any(ContactBaseDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> contactService.save(contactDTO));
        verify(contactDao, times(1)).save(any(Contact.class));
    }

    @Test
    void testFindById() {
        when(contactDao.findById(1L)).thenReturn(Optional.of(contact));
        when(contactBaseMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactBaseDTO result = contactService.findById(1L);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(contactDao, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(contactDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContactException.class, () -> contactService.findById(1L));
        verify(contactDao, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        when(contactDao.findAll()).thenReturn(Collections.singletonList(contact));
        when(contactBaseMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contactDTO));

        List<ContactBaseDTO> result = contactService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contactDao, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(contactDao.findAll()).thenReturn(Collections.emptyList());
        when(contactBaseMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ContactBaseDTO> result = contactService.findAll();
        assertTrue(result.isEmpty());

        verify(contactDao, times(1)).findAll();
    }

    @Test
    void testUpdateContact() {
        when(contactDao.findById(anyLong())).thenReturn(Optional.of(contact));
        when(contactBaseMapper.toEntity(any(ContactBaseDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenReturn(contact);
        when(contactBaseMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactBaseDTO result = contactService.update(contactDTO);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(contactDao, times(1)).findById(anyLong());
        verify(contactDao, times(1)).save(any(Contact.class));
    }

    @Test
    void testUpdateContactNotFound() {
        when(contactDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ContactException.class, () -> contactService.update(contactDTO));
        verify(contactDao, times(1)).findById(anyLong());
    }
}
