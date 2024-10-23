package pl.com.chrzanowski.sma.email;

import pl.com.chrzanowski.sma.auth.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenDTO;

import java.util.Locale;

public interface SentEmailService {
    MessageResponse sendAfterRegistration(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendAfterEmailConfirmation(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendPasswordResetMail(UserTokenDTO userTokenDTO, Locale locale);

    MessageResponse sendAfterPasswordChange(UserTokenDTO userTokenDTO, Locale locale);
}
