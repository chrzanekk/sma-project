package pl.com.chrzanowski.sma.scaffolding.dimension.dto;

import pl.com.chrzanowski.sma.unit.dto.UnitBaseDTO;

import java.math.BigDecimal;

public record DimensionResult(UnitBaseDTO unit, BigDecimal value) {
}
