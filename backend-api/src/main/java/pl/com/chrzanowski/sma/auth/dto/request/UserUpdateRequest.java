package pl.com.chrzanowski.sma.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.com.chrzanowski.sma.position.dto.PositionBaseDTO;

import java.util.List;

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
    PositionBaseDTO position;
    Boolean locked;
    Boolean enabled;
    List<String> roles;
    List<Long> companiesIds;
}
