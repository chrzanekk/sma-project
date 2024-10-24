package pl.com.chrzanowski.sma.auth.service;

import pl.com.chrzanowski.sma.auth.dto.request.NewPasswordPutRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.usertoken.dto.UserTokenDTO;

public interface PasswordResetService {

    MessageResponse saveNewPassword(UserTokenDTO userTokenDTO, NewPasswordPutRequest request);
}
