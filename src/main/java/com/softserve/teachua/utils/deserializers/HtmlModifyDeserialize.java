package com.softserve.teachua.utils.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.softserve.teachua.utils.HtmlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Use this class with @JsonDeserialize(using = HtmlModifyDeserialize.class) on DTO field to change html code.
 *
 * @author Roman Klymus
 */
public class HtmlModifyDeserialize extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String html = jsonParser.getText();
        if (html == null) {
            return null;
        }
        Document document = Jsoup.parse(html);
        document.getElementsByTag("img").forEach(element -> element.addClass("desc-img"));
        return Jsoup.clean(document.toString(), HtmlUtils.DESC_SAFELIST);
    }
}
