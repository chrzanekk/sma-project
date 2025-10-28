package pl.com.chrzanowski.sma.common.security.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUpdateRequest {
    private Long resourceId;
    private List<String> roleNames; // Nazwy ról do przypisania
}