package com.softserve.teachua.utils.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Use this class with @JsonDeserialize(using = TrimDeserialize.class) on DTO field to trim text.
 *
 * @author Roman Klymus
 */
public class TrimDeserialize extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return jsonParser.getText().trim();
    }
}
