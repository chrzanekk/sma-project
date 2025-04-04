package pl.com.chrzanowski.sma.company.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyFilter {
    private Long id;
    private String nameStartsWith;
}
