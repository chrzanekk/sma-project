package pl.com.chrzanowski.sma.user.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.com.chrzanowski.sma.auth.dto.request.RegisterRequest;
import pl.com.chrzanowski.sma.auth.dto.response.UserInfoResponse;
import pl.com.chrzanowski.sma.user.service.filter.UserFilter;
import pl.com.chrzanowski.sma.user.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO register(RegisterRequest request);

    String confirm(String token);

    UserDTO save(UserDTO userDTO);

    UserDTO update(UserDTO userDTO);

    List<UserDTO> findByFilter(UserFilter filter);

    Page<UserDTO> findByFilterAndPage(UserFilter filter, Pageable pageable);

    UserDTO findById(Long id);

    List<UserDTO> findAll();

    void delete(Long id);

    UserDTO getUser(String email);

    Boolean isUserExists(String userName);

    Boolean isEmailExists(String email);

    UserInfoResponse getUserWithAuthorities();


}
