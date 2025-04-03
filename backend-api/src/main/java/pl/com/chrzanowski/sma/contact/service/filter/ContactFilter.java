package pl.com.chrzanowski.sma.contact.service.filter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ContactFilter {

    private Long id;
    private String firstNameStartsWith;
    private String lastNameStartsWith;
    private String emailStartsWith;
    private String phoneStartsWith;
    private Long companyId;
}
