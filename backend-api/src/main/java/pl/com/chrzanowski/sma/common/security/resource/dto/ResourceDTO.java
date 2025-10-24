package pl.com.chrzanowski.sma.common.security.resource.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private Long id;
    private String resourceKey;
    private String displayName;
    private String endpointPattern;
    private String description;
    private String httpMethod;
    private Boolean isActive;
    private Boolean isPublic;
    private List<String> allowedRoles; // Lista nazw ról
}