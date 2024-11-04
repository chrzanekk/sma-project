package pl.com.chrzanowski.sma.unitTests.email.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.com.chrzanowski.sma.email.dao.SendEmailJPADaoImpl;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.email.repository.SendEmailRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendEmailJPADaoImplTest {

    @InjectMocks
    private SendEmailJPADaoImpl sendEmailJPADaoImpl;

    @Mock
    private SendEmailRepository sendEmailRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void save_Positive() {
        // Given
        SendEmail sendEmail = SendEmail.builder().title("Test Email").content("This is a test email.").build();

        // When
        sendEmailJPADaoImpl.save(sendEmail);

        // Then
        verify(sendEmailRepository, times(1)).save(sendEmail);
    }


    @Test
    void save_Negative() {
        // Given
        SendEmail sendEmail = SendEmail.builder().title("Test Email").content("This is a test email.").build();
        doThrow(new RuntimeException("Save failed")).when(sendEmailRepository).save(sendEmail);

        // When / Then
        assertThrows(RuntimeException.class, () -> sendEmailJPADaoImpl.save(sendEmail));
        verify(sendEmailRepository, times(1)).save(sendEmail);
    }
}