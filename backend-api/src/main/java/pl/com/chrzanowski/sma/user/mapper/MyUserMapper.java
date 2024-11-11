package pl.com.chrzanowski.sma.user.mapper;

import org.springframework.stereotype.Service;
import pl.com.chrzanowski.sma.user.dto.UserDTO;
import pl.com.chrzanowski.sma.user.model.User;

@Service
public class MyUserMapper {

    public User userDTOtoUser(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
//            user.setRoles(userDTO.getRoles());
            return user;
        }
    }
}
