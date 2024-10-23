package pl.com.chrzanowski.sma.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class DictionaryDTO {
    Long id;
    String code;
    String value;
    String language;
    Long extraId;
    BigDecimal extraPrice;
    String extraString;
}
