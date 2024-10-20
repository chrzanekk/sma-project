package pl.com.chrzanowski.scma.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxCalculationUtil {
    public static BigDecimal calculateTaxValue(BigDecimal netValue, BigDecimal taxRate) {
        return calculateGrossValue(netValue, taxRate).subtract(netValue).setScale(2, RoundingMode.HALF_EVEN);

    }

    public static BigDecimal calculateGrossValue(BigDecimal netValue, BigDecimal taxRate) {
        return netValue.multiply((BigDecimal.ONE.add(taxRate)));
    }
}
