package pl.com.chrzanowski.sma.user.service;


import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.MessageResponse;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.user.dto.AdminEditPasswordChangeRequest;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.auth.dto.request.UserEditPasswordChangeRequest;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDTO register(RegisterRequest request);

    MessageResponse confirm(String token);

    UserInfoResponse getUserWithAuthorities();

    UserDTO updateUserRoles(Long userId, Set<RoleDTO> roles);

    void updateUserPassword(UserEditPasswordChangeRequest userEditPasswordChangeRequest);

    UserDTO save(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    UserDTO findById(Long id);

    List<UserDTO> findAll();

    void delete(Long id);

    UserDTO getUserByEmail(String email);

    UserDTO getUserByLogin(String login);

    Boolean isUserExists(String userName);

    Boolean isEmailExists(String email);

    void updateUserPasswordByAdmin(AdminEditPasswordChangeRequest adminEditPasswordChangeRequest);
}
