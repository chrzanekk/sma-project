package pl.com.chrzanowski.sma.common.json;

import pl.com.chrzanowski.sma.common.enumeration.Country;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;

public class CountryDeserializer extends JsonDeserializer<Country> {

    @Override
    public Country deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.has("code")) {
            String code = node.get("code").asText();
            return Country.fromCode(code);
        }
        return null;
    }
}
