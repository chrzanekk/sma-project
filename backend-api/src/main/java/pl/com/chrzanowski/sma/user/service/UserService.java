package pl.com.chrzanowski.sma.user.service;


import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.dto.UserPasswordChangeRequest;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDTO register(RegisterRequest request);

    String confirm(String token);

    UserDTO save(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    void updateUserPassword(UserPasswordChangeRequest userPasswordChangeRequest);

    UserDTO updateUserRoles(Long userId, Set<RoleDTO> roles);

    UserDTO findById(Long id);

    List<UserDTO> findAll();

    void delete(Long id);

    UserDTO getUser(String email);

    Boolean isUserExists(String userName);

    Boolean isEmailExists(String email);

    UserInfoResponse getUserWithAuthorities();


}
