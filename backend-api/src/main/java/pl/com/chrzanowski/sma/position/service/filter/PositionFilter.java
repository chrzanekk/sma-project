package pl.com.chrzanowski.sma.position.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class PositionFilter {

    private Long id;
    private String nameContains;
    private String descriptionContains;
    private Long companyId;
}
