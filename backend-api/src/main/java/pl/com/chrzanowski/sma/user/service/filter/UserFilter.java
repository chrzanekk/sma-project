package pl.com.chrzanowski.sma.user.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class UserFilter {

    private Long id;
    private String emailStartsWith;
    private String loginStartsWith;
    private String firstNameStartsWith;
    private String lastNameStartsWith;
    private String positionStartsWith;
    private Boolean isLocked;
    private Boolean isEnabled;
    private List<String> roles;
}
