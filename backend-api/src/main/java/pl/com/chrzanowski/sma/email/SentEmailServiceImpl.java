package pl.com.chrzanowski.sma.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.com.chrzanowski.sma.auth.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenDTO;
import pl.com.chrzanowski.sma.dictionary.DictionaryDTO;
import pl.com.chrzanowski.sma.dictionary.DictionaryService;
import pl.com.chrzanowski.sma.enumeration.DictionaryType;
import pl.com.chrzanowski.sma.enumeration.Language;
import pl.com.chrzanowski.sma.enumeration.MailEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class SentEmailServiceImpl implements SentEmailService {

    @Value("${jwt.tokenValidityTimeInMinutes}")
    private Long tokenValidityTimeInMinutes;

    @Value("${platform.url}")
    private String scaffoldingAppUrl;

    private final Logger log = LoggerFactory.getLogger(SentEmailServiceImpl.class);

    private static final String LOGIN_PAGE_URL = "loginPageUrl";

    private final EmailSenderService emailSenderService;
    private final SentEmailRepository sentEmailRepository;
    private final DictionaryService dictionaryService;
    private final TemplateEngine templateEngine;
    private final SentEmailMapper sentEmailMapper;

    public SentEmailServiceImpl(EmailSenderService emailSenderService,
                                SentEmailRepository sentEmailRepository,
                                DictionaryService dictionaryService,
                                TemplateEngine templateEngine,
                                SentEmailMapper sentEmailMapper) {
        this.emailSenderService = emailSenderService;
        this.sentEmailRepository = sentEmailRepository;
        this.dictionaryService = dictionaryService;
        this.templateEngine = templateEngine;
        this.sentEmailMapper = sentEmailMapper;
    }

    //todo implement confirmation information in frontend
    //todo find way to pass frontend domain name here when application will be deployed to production
    @Override
    public MessageResponse sendAfterRegistration(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user registration: {}", userTokenDTO.getEmail());
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
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
                .language(Language.from(locale.getLanguage())).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sentEmailRepository.save(sentEmail);
        return new MessageResponse("Register successful");
    }

    @Override
    public MessageResponse sendAfterEmailConfirmation(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm user activation:");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
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
                .createDatetime(LocalDateTime.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sentEmailRepository.save(sentEmail);
        return new MessageResponse("Confirmed successful");
    }

    @Override
    public MessageResponse sendPasswordResetMail(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to reset password");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
        context.setVariable("passwordResetLink",
                scaffoldingAppUrl + "/account/password-reset?token=" + userTokenDTO.getToken());
        context.setVariable("tokenValidityTime", tokenValidityTimeInMinutes);
        String content = templateEngine.process("mail-password-reset", context);
        String title = chooseTitle(MailEvent.PASSWORD_RESET, locale);
        SentEmailDTO sentEmailDTO = SentEmailDTO.builder()
                .userId(userTokenDTO.getUserId())
                .userEmail(userTokenDTO.getEmail())
                .title(title)
                .content(content)
                .mailEvent(MailEvent.PASSWORD_RESET)
                .language(Language.from(locale.getLanguage())).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sentEmailRepository.save(sentEmail);
        return new MessageResponse("Password reset token sent with token: " + userTokenDTO.getToken());
    }

    @Override
    public MessageResponse sendAfterPasswordChange(UserTokenDTO userTokenDTO, Locale locale) {
        log.debug("Request to send email to confirm password reset.");
        Context context = new Context(locale);
        context.setVariable(LOGIN_PAGE_URL, scaffoldingAppUrl + "/account/login");
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
                .createDatetime(LocalDateTime.now()).build();
        emailSenderService.sendEmail(sentEmailDTO);
        SentEmail sentEmail = sentEmailMapper.toEntity(sentEmailDTO);
        sentEmailRepository.save(sentEmail);
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
}
