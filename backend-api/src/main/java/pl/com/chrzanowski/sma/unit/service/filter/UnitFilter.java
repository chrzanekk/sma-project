package pl.com.chrzanowski.sma.unit.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class UnitFilter {

    private Long id;
    private String symbolContains;
    private String descriptionContains;
    private String unitType;
    private Long companyId;
}
