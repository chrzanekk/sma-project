package pl.com.chrzanowski.sma.auth;

import pl.com.chrzanowski.sma.auth.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.usertokens.UserTokenDTO;

public interface PasswordResetService {

    MessageResponse saveNewPassword(UserTokenDTO userTokenDTO, NewPasswordPutRequest request);
}
