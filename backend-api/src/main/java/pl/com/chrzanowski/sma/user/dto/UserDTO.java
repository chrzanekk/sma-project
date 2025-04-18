package pl.com.chrzanowski.sma.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.com.chrzanowski.sma.company.dto.CompanyBaseDTO;
import pl.com.chrzanowski.sma.role.dto.RoleDTO;

import java.time.Instant;
import java.util.Set;

@Builder(toBuilder = true)
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserDTO {

    Long id;
    String email;
    String login;
    String password;
    String firstName;
    String lastName;
    String position;
    Boolean locked;
    Boolean enabled;
    Set<RoleDTO> roles;
    Set<CompanyBaseDTO> companies;
    Instant createdDatetime;
    Instant lastModifiedDatetime;
}
