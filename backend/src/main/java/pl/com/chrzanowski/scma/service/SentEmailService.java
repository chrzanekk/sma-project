package pl.com.chrzanowski.scma.service;

import pl.com.chrzanowski.scma.payload.response.MessageResponse;
import pl.com.chrzanowski.scma.service.dto.ConfirmationTokenDTO;
import pl.com.chrzanowski.scma.service.dto.PasswordResetTokenDTO;

import java.util.Locale;

public interface SentEmailService {
    MessageResponse sendAfterRegistration(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterEmailConfirmation(ConfirmationTokenDTO confirmationTokenDTO, Locale locale);

    MessageResponse sendAfterPasswordChange(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);

    MessageResponse sendPasswordResetMail(PasswordResetTokenDTO passwordResetTokenDTO, Locale locale);
}
