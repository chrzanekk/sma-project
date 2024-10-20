package pl.com.chrzanowski.scma.service.dto;

import java.math.BigDecimal;

public class DictionaryDTO {
    private final Long id;
    private final String code;
    private final String value;
    private final String language;
    private final Long extraId;
    private final BigDecimal extraPrice;
    private final String extraString;

    public DictionaryDTO(Long id,
                         String code,
                         String value,
                         String language,
                         Long extraId,
                         BigDecimal extraPrice,
                         String extraString) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.language = language;
        this.extraId = extraId;
        this.extraPrice = extraPrice;
        this.extraString = extraString;
    }

    private DictionaryDTO(Builder builder) {
        id = builder.id;
        code = builder.code;
        value = builder.value;
        language = builder.language;
        extraId = builder.extraId;
        extraPrice = builder.extraPrice;
        extraString = builder.extraString;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(DictionaryDTO copy) {
        Builder builder = new Builder();
        builder.id = copy.getId();
        builder.code = copy.getCode();
        builder.value = copy.getValue();
        builder.language = copy.getLanguage();
        builder.extraId = copy.getExtraId();
        builder.extraPrice = copy.getExtraPrice();
        builder.extraString = copy.getExtraString();
        return builder;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getLanguage() {
        return language;
    }

    public Long getExtraId() {
        return extraId;
    }

    public BigDecimal getExtraPrice() {
        return extraPrice;
    }

    public String getExtraString() {
        return extraString;
    }


    public static final class Builder {
        private Long id;
        private String code;
        private String value;
        private String language;
        private Long extraId;
        private BigDecimal extraPrice;
        private String extraString;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder extraId(Long extraId) {
            this.extraId = extraId;
            return this;
        }

        public Builder extraPrice(BigDecimal extraPrice) {
            this.extraPrice = extraPrice;
            return this;
        }

        public Builder extraString(String extraString) {
            this.extraString = extraString;
            return this;
        }

        public DictionaryDTO build() {
            return new DictionaryDTO(this);
        }
    }
}
