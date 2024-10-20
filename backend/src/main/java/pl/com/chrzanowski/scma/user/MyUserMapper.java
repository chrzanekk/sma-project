package pl.com.chrzanowski.scma.user;

import org.springframework.stereotype.Service;

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
