package pl.com.chrzanowski.sma.scaffolding.position.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScaffoldingLogPositionPageDTO {
    private List<ScaffoldingLogPositionAuditableDTO> parents;
    private List<ScaffoldingLogPositionAuditableDTO> children;
    private long totalElements;
    private int totalPages;
}