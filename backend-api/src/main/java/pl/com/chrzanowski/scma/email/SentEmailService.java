package pl.com.chrzanowski.scma.email;

import pl.com.chrzanowski.scma.auth.response.MessageResponse;
import pl.com.chrzanowski.scma.auth.confirmationtoken.ConfirmationTokenDTO;
import pl.com.chrzanowski.scma.auth.resettoken.PasswordResetTokenDTO;

import java.util.Locale;

public interface SentEmailService {
    MessageResponse sendAfterRegistration(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterEmailConfirmation(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterPasswordChange(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);

    MessageResponse sendPasswordResetMail(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);
}
