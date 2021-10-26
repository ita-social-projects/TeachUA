package com.softserve.teachua.utils;


import com.softserve.teachua.exception.IncorrectInputException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

/**
 * This class is used to validate html code using JSOUP library.
 * For documentation, go to jsoup.org.
 *
 * @author Roman Klymus
 */
public class HtmlValidator {

    private static final String FORBIDDEN_DESC_TAGS = "You have used forbidden tags or attributes. " +
            "Only allow the following: a, b, blockquote, br, caption, cite, code, col, colgroup, " +
            "dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, span, " +
            "strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul." +
            "Also you can use form tag only with action=\"link\" attribute," +
            "input with type=\"submit\" value=\"Go to link\" to add a button to description and " +
            "div and span with style to decorate description.";
    private static final Safelist DESC_SAFELIST = Safelist.relaxed()
            .addTags("form").addAttributes("form", "action")
            .addTags("input").addAttributes("input", "type", "value", "class")
            .addTags("/input")
            .addAttributes("div", "style")
            .addAttributes("span", "style")
            .addAttributes("a", "href")
            .addAttributes("a", "style")
            .addAttributes("div", "class");


    /**
     * This method is used to validate the description of challenges and tasks.
     * Use {@code addTags} and {@code addAttributes} on {@code Safelist} class
     * to add new tags and attributes to whitelist. And don't forget to update
     * error message.
     *
     * @param desc put html code to validate
     * @see Safelist
     * @throws IncorrectInputException throws if the code has forbidden tags and attributes
     */
    public static void validateDescription(String desc) {
        System.out.println(Jsoup.clean(desc, DESC_SAFELIST)); //Use this to debug which tags are not valid
        if (!Jsoup.isValid(desc, DESC_SAFELIST)) {
            throw new IncorrectInputException(FORBIDDEN_DESC_TAGS);
        }
        Document document = Jsoup.parse(desc);
        if(document.getElementsByTag("input").stream()
                .anyMatch(element -> !element.attr("type").equals("submit")))
        {
            throw new IncorrectInputException(FORBIDDEN_DESC_TAGS);
        }
    }
}
