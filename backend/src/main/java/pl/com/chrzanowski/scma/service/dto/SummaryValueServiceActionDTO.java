package pl.com.chrzanowski.scma.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class SummaryValueServiceActionDTO {

    private BigDecimal summaryGrossValue;
    private BigDecimal summaryTaxValue;
    private BigDecimal summaryNetValue;

    public SummaryValueServiceActionDTO() {
    }

    public SummaryValueServiceActionDTO(BigDecimal summaryGrossValue,
                                        BigDecimal summaryTaxValue,
                                        BigDecimal summaryNetValue) {
        this.summaryGrossValue = summaryGrossValue;
        this.summaryTaxValue = summaryTaxValue;
        this.summaryNetValue = summaryNetValue;
    }

    private SummaryValueServiceActionDTO(Builder builder) {
        summaryGrossValue = builder.summaryGrossValue;
        summaryTaxValue = builder.summaryTaxValue;
        summaryNetValue = builder.summaryNetValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SummaryValueServiceActionDTO copy) {
        Builder builder = new Builder();
        builder.summaryGrossValue = copy.getSummaryGrossValue();
        builder.summaryTaxValue = copy.getSummaryTaxValue();
        builder.summaryNetValue = copy.getSummaryNetValue();
        return builder;
    }

    public BigDecimal getSummaryGrossValue() {
        return summaryGrossValue;
    }

    public BigDecimal getSummaryTaxValue() {
        return summaryTaxValue;
    }

    public BigDecimal getSummaryNetValue() {
        return summaryNetValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SummaryValueServiceActionDTO that = (SummaryValueServiceActionDTO) o;

        if (!Objects.equals(summaryGrossValue, that.summaryGrossValue))
            return false;
        if (!Objects.equals(summaryTaxValue, that.summaryTaxValue))
            return false;
        return Objects.equals(summaryNetValue, that.summaryNetValue);
    }

    @Override
    public int hashCode() {
        int result = summaryGrossValue != null ? summaryGrossValue.hashCode() : 0;
        result = 31 * result + (summaryTaxValue != null ? summaryTaxValue.hashCode() : 0);
        result = 31 * result + (summaryNetValue != null ? summaryNetValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SummaryValueServiceActionDTO{" +
                "summaryGrossValue=" + summaryGrossValue +
                ", summaryTaxValue=" + summaryTaxValue +
                ", summaryNetValue=" + summaryNetValue +
                '}';
    }


    public static final class Builder {
        private BigDecimal summaryGrossValue;
        private BigDecimal summaryTaxValue;
        private BigDecimal summaryNetValue;

        private Builder() {
        }

        public Builder summaryGrossValue(BigDecimal summaryGrossValue) {
            this.summaryGrossValue = summaryGrossValue;
            return this;
        }

        public Builder summaryTaxValue(BigDecimal summaryTaxValue) {
            this.summaryTaxValue = summaryTaxValue;
            return this;
        }

        public Builder summaryNetValue(BigDecimal summaryNetValue) {
            this.summaryNetValue = summaryNetValue;
            return this;
        }

        public SummaryValueServiceActionDTO build() {
            return new SummaryValueServiceActionDTO(this);
        }
    }
}
