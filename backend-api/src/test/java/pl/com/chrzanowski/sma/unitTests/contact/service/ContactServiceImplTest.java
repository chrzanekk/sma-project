package pl.com.chrzanowski.sma.unitTests.contact.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.common.exception.ContactException;
import pl.com.chrzanowski.sma.common.exception.ObjectNotFoundException;
import pl.com.chrzanowski.sma.contact.dao.ContactDao;
import pl.com.chrzanowski.sma.contact.dto.ContactDTO;
import pl.com.chrzanowski.sma.contact.mapper.ContactMapper;
import pl.com.chrzanowski.sma.contact.model.Contact;
import pl.com.chrzanowski.sma.contact.service.ContactServiceImpl;
import pl.com.chrzanowski.sma.user.model.User;
import pl.com.chrzanowski.sma.user.service.UserService;

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
    private ContactMapper contactMapper;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ContactServiceImpl contactService;

    private ContactDTO contactDTO;
    private Contact contact;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        contactDTO = ContactDTO.builder()
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

        UserInfoResponse mockUser = new UserInfoResponse(1L, "login", "email@email.com", "test", "user", "position", null);
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
    void testSaveContact() {
        when(contactMapper.toEntity(any(ContactDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenReturn(contact);
        when(contactMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactDTO result = contactService.save(contactDTO);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("555-555-555", result.getPhoneNumber());
        assertEquals("john@doe.com", result.getEmail());

        verify(contactDao, times(1)).save(any(Contact.class));
        verify(contactMapper, times(1)).toDto(any(Contact.class));
    }

    @Test
    void testSaveContactFailure() {
        when(contactMapper.toEntity(any(ContactDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> contactService.save(contactDTO));
        verify(contactDao, times(1)).save(any(Contact.class));
    }

    @Test
    void testFindById() {
        when(contactDao.findById(1L)).thenReturn(Optional.of(contact));
        when(contactMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactDTO result = contactService.findById(1L);
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
        when(contactMapper.toDtoList(anyList())).thenReturn(Collections.singletonList(contactDTO));

        List<ContactDTO> result = contactService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(contactDao, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(contactDao.findAll()).thenReturn(Collections.emptyList());
        when(contactMapper.toDtoList(anyList())).thenReturn(Collections.emptyList());

        List<ContactDTO> result = contactService.findAll();
        assertTrue(result.isEmpty());

        verify(contactDao, times(1)).findAll();
    }

    @Test
    void testUpdateContact() {
        when(contactDao.findById(anyLong())).thenReturn(Optional.of(contact));
        when(contactMapper.toEntity(any(ContactDTO.class))).thenReturn(contact);
        when(contactDao.save(any(Contact.class))).thenReturn(contact);
        when(contactMapper.toDto(any(Contact.class))).thenReturn(contactDTO);

        ContactDTO result = contactService.update(contactDTO);
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
