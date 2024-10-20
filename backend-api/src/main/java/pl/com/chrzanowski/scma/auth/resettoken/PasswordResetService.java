package pl.com.chrzanowski.scma.auth.resettoken;

import pl.com.chrzanowski.scma.auth.request.NewPasswordPutRequest;
import pl.com.chrzanowski.scma.auth.response.MessageResponse;

public interface PasswordResetService {

    MessageResponse saveNewPassword(PasswordResetTokenDTO passwordResetTokenDTO, NewPasswordPutRequest request);
}
