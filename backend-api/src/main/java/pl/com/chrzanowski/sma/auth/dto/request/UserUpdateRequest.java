package pl.com.chrzanowski.sma.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserUpdateRequest {

    Long id;
    String email;
    String login;
    String firstName;
    String lastName;
    String position;
    Boolean locked;
    Boolean enabled;
    List<String> roles;
}
