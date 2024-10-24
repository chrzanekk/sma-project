package pl.com.chrzanowski.scma.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryDTO;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryService;
import pl.com.chrzanowski.sma.common.enumeration.DictionaryType;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;
import pl.com.chrzanowski.sma.email.dto.SentEmailDTO;
import pl.com.chrzanowski.sma.email.mapper.SentEmailMapper;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.email.repository.SendEmailRepository;
import pl.com.chrzanowski.sma.email.service.EmailSenderService;
import pl.com.chrzanowski.sma.email.service.SendEmailServiceImpl;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SendEmailServiceImplTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private SendEmailRepository sendEmailRepository;

    @Mock
    private DictionaryService dictionaryService;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private SentEmailMapper sentEmailMapper;

    @InjectMocks
    private SendEmailServiceImpl sentEmailService;

    private UserTokenDTO userTokenDTO;
    private Locale locale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .userId(1L)
                .token("token123")
                .build();
        locale = new Locale("en");
    }

    @Test
    void testSendAfterRegistration() {
        // Mockowanie odpowiedzi z templateEngine
        when(templateEngine.process(eq("mail-after-registration"), any(Context.class)))
                .thenReturn("Email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.AFTER_REGISTRATION.getCode())
                .value("Registration Confirmation")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));

        // Mockowanie metody mappera
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title("Registration Confirmation")
                .content("Email content")
                .mailEvent(MailEvent.AFTER_REGISTRATION)
                .language(Language.ENGLISH)
                .build();
        SendEmail sendEmail = new SendEmail();
        when(sentEmailMapper.toEntity(any(SentEmailDTO.class))).thenReturn(sendEmail);

        // Wywołanie metody
        MessageResponse response = sentEmailService.sendAfterRegistration(userTokenDTO, locale);

        // Sprawdzanie czy email został wysłany
        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));

        // Sprawdzanie czy rekord został zapisany
        verify(sendEmailRepository).save(sendEmail);

        // Assercja na odpowiedzi
        assertEquals("Register successful", response.getMessage());
    }

    @Test
    void testSendAfterEmailConfirmation() {
        // Mockowanie odpowiedzi z templateEngine
        when(templateEngine.process(eq("mail-after-confirmation"), any(Context.class)))
                .thenReturn("Confirmation email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.AFTER_CONFIRMATION.getCode())
                .value("Confirmation Email")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));


        // Mockowanie metody mappera
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .content("Confirmation email content")
                .title("Confirmation Email")
                .mailEvent(MailEvent.AFTER_CONFIRMATION)
                .language(Language.ENGLISH)
                .build();
        SendEmail sendEmail = new SendEmail();
        when(sentEmailMapper.toEntity(any(SentEmailDTO.class))).thenReturn(sendEmail);

        // Wywołanie metody
        MessageResponse response = sentEmailService.sendAfterEmailConfirmation(userTokenDTO, locale);

        // Sprawdzanie czy email został wysłany
        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));

        // Sprawdzanie czy rekord został zapisany
        verify(sendEmailRepository).save(sendEmail);

        // Assercja na odpowiedzi
        assertEquals("Confirmed successful", response.getMessage());
    }
}
