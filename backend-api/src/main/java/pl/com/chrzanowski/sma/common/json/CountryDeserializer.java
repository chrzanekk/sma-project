package pl.com.chrzanowski.sma.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.io.IOException;

public class CountryDeserializer extends JsonDeserializer<Country> {
    @Override
    public Country deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        var node = p.getCodec().readTree(p);
        String code = node.get("code").toString();
        return Country.getByType(code); // Pobiera Country na podstawie kodu
    }
}
