package pl.com.chrzanowski.sma.email.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryDTO;
import pl.com.chrzanowski.sma.common.dictionary.DictionaryService;
import pl.com.chrzanowski.sma.common.enumeration.DictionaryType;
import pl.com.chrzanowski.sma.common.enumeration.Language;
import pl.com.chrzanowski.sma.common.enumeration.MailEvent;
import pl.com.chrzanowski.sma.email.dao.SendEmailDao;
import pl.com.chrzanowski.sma.email.dto.SentEmailDTO;
import pl.com.chrzanowski.sma.email.mapper.SentEmailMapper;
import pl.com.chrzanowski.sma.email.model.SendEmail;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class SendEmailServiceImpl implements SendEmailService {

    @Value("${jwt.tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    @Value("${platform.url}")
    private String scaffoldingAppUrl;

    private final Logger log = LoggerFactory.getLogger(SendEmailServiceImpl.class);

    private static final String LOGIN_PAGE_URL = "loginPageUrl";

    private final EmailSenderService emailSenderService;
    private final DictionaryService dictionaryService;
    private final TemplateEngine templateEngine;
    private final SentEmailMapper sentEmailMapper;
    private final SendEmailDao sendEmailDao;

    public SendEmailServiceImpl(EmailSenderService emailSenderService,
                                DictionaryService dictionaryService,
                                TemplateEngine templateEngine,
                                SentEmailMapper sentEmailMapper, SendEmailDao sendEmailDao) {
        this.emailSenderService = emailSenderService;

        this.dictionaryService = dictionaryService;
        this.templateEngine = templateEngine;
        this.sentEmailMapper = sentEmailMapper;
        this.sendEmailDao = sendEmailDao;
    }

    @Override
    @Transactional
    public MessageResponse sendAfterRegistration(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user registration: {}", userTokenDTO.getEmail());
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl);
        context.setVariable("emailConfirmationLink",
                scaffoldingAppUrl + "/confirm?token=" + userTokenDTO.getToken());
        context.setVariable("tokenValidityTime", tokenValidityTimeInMinutes);
        //template to send as string
        String content = templateEngine.process("mail-after-registration", context);
        String title = chooseTitle(MailEvent.AFTER_REGISTRATION, locale);
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title(title)
                .content(content)
                .mailEvent(MailEvent.AFTER_REGISTRATION)
                .language(Language.from(locale.getLanguage()))
                .createDatetime(Instant.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SendEmail sendEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sendEmailDao.save(sendEmail);
        return new MessageResponse("Register successful");
    }

    @Override
    @Transactional
    public MessageResponse sendAfterEmailConfirmation(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user activation:");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl);
        //template to send as string
        String content = templateEngine.process("mail-after-confirmation", context);
        String title = chooseTitle(MailEvent.AFTER_CONFIRMATION, locale);
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title(title)
                .content(content)
                .mailEvent(MailEvent.AFTER_CONFIRMATION)
                .language(Language.from(locale.getLanguage()))
                .createDatetime(Instant.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SendEmail sendEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sendEmailDao.save(sendEmail);
        return new MessageResponse("Confirmed successful");
    }

    @Override
    @Transactional
    public MessageResponse sendPasswordResetMail(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to reset password");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl);
        context.setVariable("passwordResetLink",
                scaffoldingAppUrl + "/reset-password?token=" + userTokenDTO.getToken());
        context.setVariable("tokenValidityTime", tokenValidityTimeInMinutes);
        String content = templateEngine.process("mail-password-reset", context);
        String title = chooseTitle(MailEvent.PASSWORD_RESET, locale);
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title(title)
                .content(content)
                .mailEvent(MailEvent.PASSWORD_RESET)
                .language(Language.from(locale.getLanguage()))
                .createDatetime(Instant.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SendEmail sendEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sendEmailDao.save(sendEmail);
        return new MessageResponse("Password reset token sent");
    }

    @Override
    @Transactional
    public MessageResponse sendAfterPasswordChange(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm password reset.");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl);
        //template to send as string
        String content = templateEngine.process("mail-after-password-change", context);
        String title = chooseTitle(MailEvent.AFTER_PASSWORD_CHANGE, locale);
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title(title)
                .content(content)
                .mailEvent(MailEvent.AFTER_PASSWORD_CHANGE)
                .language(Language.from(locale.getLanguage()))
                .createDatetime(Instant.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SendEmail sendEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sendEmailDao.save(sendEmail);
        return new MessageResponse("Password changed successfully");
    }

    private String chooseTitle(MailEvent mailEvent, Locale locale) {

        List<DictionaryDTO> list = dictionaryService.getDictionary(DictionaryType.EMAIL_TITLES, locale);

        for (DictionaryDTO dictionaryData : list) {
            if (dictionaryData.getCode().equals(mailEvent.getCode())) {
                return dictionaryData.getValue();
            }
        }
        throw new IllegalArgumentException("No email title!");
    }

    @Override
    @Transactional
    public void deleteEmailByUserId(Long userId) {
        log.debug("Request to delete emails by userId {}", userId);
        sendEmailDao.deleteByUserId(userId);
    }
}
