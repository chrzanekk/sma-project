package pl.com.chrzanowski.sma.unitTests.email.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.email.dao.SendEmailDao;
import pl.com.chrzanowski.sma.email.service.EmailSenderService;
import pl.com.chrzanowski.sma.email.service.SendEmailServiceImpl;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryDTO;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryService;
import pl.com.chrzanowski.sma.common.enumeration.DictionaryType;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;
import pl.com.chrzanowski.sma.email.dto.SentEmailDTO;
import pl.com.chrzanowski.sma.email.mapper.SentEmailMapper;
import pl.com.chrzanowski.sma.email.model.SendEmail;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SendEmailServiceImplTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private SendEmailDao sendEmailDao;

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
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userTokenDTO = UserTokenDTO.builder()
                .email("test@example.com")
                .userId(1L)
                .token("token123")
                .build();
        locale = new Locale("en");
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSendAfterRegistration() {
        when(templateEngine.process(eq("mail-after-registration"), any(Context.class)))
                .thenReturn("Email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.AFTER_REGISTRATION.getCode())
                .value("Registration Confirmation")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));

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

        MessageResponse response = sentEmailService.sendAfterRegistration(userTokenDTO, locale);

        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));
        verify(sendEmailDao).save(sendEmail);

        assertEquals("Register successful", response.getMessage());
    }

    @Test
    void testSendAfterEmailConfirmation() {
        when(templateEngine.process(eq("mail-after-confirmation"), any(Context.class)))
                .thenReturn("Confirmation email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.AFTER_CONFIRMATION.getCode())
                .value("Confirmation Email")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));

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


        MessageResponse response = sentEmailService.sendAfterEmailConfirmation(userTokenDTO, locale);


        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));

        verify(sendEmailDao).save(sendEmail);


        assertEquals("Confirmed successful", response.getMessage());
    }

    @Test
    void testSendPasswordResetMail_Success() {
        when(templateEngine.process(eq("mail-password-reset"), any(Context.class)))
                .thenReturn("Password reset email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.PASSWORD_RESET.getCode())
                .value("Password Reset Request")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));

        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .content("Password reset email content")
                .title("Password Reset Request")
                .mailEvent(MailEvent.PASSWORD_RESET)
                .language(Language.ENGLISH)
                .build();
        SendEmail sendEmail = new SendEmail();
        when(sentEmailMapper.toEntity(any(SentEmailDTO.class))).thenReturn(sendEmail);

        MessageResponse response = sentEmailService.sendPasswordResetMail(userTokenDTO, locale);

        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));
        verify(sendEmailDao).save(sendEmail);

        assertEquals("Password reset token sent",response.getMessage());
    }

    @Test
    void testSendPasswordResetMail_TitleNotFound() {
        when(templateEngine.process(eq("mail-password-reset"), any(Context.class)))
                .thenReturn("Password reset email content");

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sentEmailService.sendPasswordResetMail(userTokenDTO, locale);
        });

        assertEquals("No email title!", exception.getMessage());
    }

    @Test
    void testSendAfterPasswordChange_Success() {
        when(templateEngine.process(eq("mail-after-password-change"), any(Context.class)))
                .thenReturn("Password change confirmation email content");

        DictionaryDTO emailTitleDictionary = DictionaryDTO.builder()
                .code(MailEvent.AFTER_PASSWORD_CHANGE.getCode())
                .value("Password Changed Successfully")
                .build();

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of(emailTitleDictionary));

        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .content("Password change confirmation email content")
                .title("Password Changed Successfully")
                .mailEvent(MailEvent.AFTER_PASSWORD_CHANGE)
                .language(Language.ENGLISH)
                .build();
        SendEmail sendEmail = new SendEmail();
        when(sentEmailMapper.toEntity(any(SentEmailDTO.class))).thenReturn(sendEmail);

        MessageResponse response = sentEmailService.sendAfterPasswordChange(userTokenDTO, locale);

        verify(emailSenderService).sendEmail(any(SentEmailDTO.class));
        verify(sendEmailDao).save(sendEmail);

        assertEquals("Password changed successfully", response.getMessage());
    }

    @Test
    void testSendAfterPasswordChange_TitleNotFound() {
        when(templateEngine.process(eq("mail-after-password-change"), any(Context.class)))
                .thenReturn("Password change confirmation email content");

        when(dictionaryService.getDictionary(eq(DictionaryType.EMAIL_TITLES), eq(locale)))
                .thenReturn(List.of()); // Pusta lista - brak tytułu

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sentEmailService.sendAfterPasswordChange(userTokenDTO, locale);
        });

        assertEquals("No email title!", exception.getMessage());
    }

}
