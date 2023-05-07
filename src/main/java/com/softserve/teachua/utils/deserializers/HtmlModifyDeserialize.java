package com.softserve.teachua.utils.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.softserve.teachua.utils.HtmlUtils;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Use this class with @JsonDeserialize(using = HtmlModifyDeserialize.class) on DTO field to change html code.
 *
 * @author Roman Klymus
 */
public class HtmlModifyDeserialize extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String html = jsonParser.getText();
        if (html == null) {
            return null;
        }
        Document document = Jsoup.parse(html);
        document.getElementsByTag("img").forEach(element -> element.addClass("desc-img"));
        return Jsoup.clean(document.toString(), HtmlUtils.DESC_SAFELIST);
    }
}
