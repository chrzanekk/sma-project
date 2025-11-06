package pl.com.chrzanowski.sma.scaffolding.worktype.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class WorkTypeFilter {
    private Long id;
    private String nameContains;
    private String descriptionContains;
}
