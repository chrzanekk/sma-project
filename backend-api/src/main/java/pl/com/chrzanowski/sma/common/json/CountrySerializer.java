package pl.com.chrzanowski.sma.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pl.com.chrzanowski.sma.common.enumeration.Country;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CountrySerializer extends JsonSerializer<Country> {

    @Override
    public void serialize(Country value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Map<String, String> countryData = new HashMap<>();
        countryData.put("code", value.getCode());
        countryData.put("name", value.getName());
        gen.writeObject(countryData);
    }

}
