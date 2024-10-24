package pl.com.chrzanowski.sma.email.service;

import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;

import java.util.Locale;

public interface SendEmailService {
    MessageResponse sendAfterRegistration(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendAfterEmailConfirmation(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendPasswordResetMail(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendAfterPasswordChange(UserTokenDTO userTokenDTO, Locale locale);
}
