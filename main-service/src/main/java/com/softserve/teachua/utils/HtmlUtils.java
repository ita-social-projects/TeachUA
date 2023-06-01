package com.softserve.teachua.utils;

import com.softserve.teachua.exception.IncorrectInputException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * This class is used to validate html code using JSOUP library. For documentation, go to jsoup.org.
 *
 * @author Roman Klymus
 */
@Slf4j
public class HtmlUtils {
    private static final String FORBIDDEN_DESC_TAGS = "You have used forbidden tags or attributes. "
            + "Only allow the following: a, b, blockquote, br, caption, cite, code, col, colgroup, "
            + "dd, div, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, span, "
            + "strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul.";
    private static final String ATTRIBUTE = "class";
    public static final Safelist DESC_SAFELIST =
            Safelist.relaxed().addTags("s", "iframe")
                    .addAttributes("span", ATTRIBUTE, "style")
                    .addAttributes("a", "href", "rel", "target")
                    .addAttributes("div", ATTRIBUTE)
                    .addAttributes("p", ATTRIBUTE)
                    .addAttributes("li", ATTRIBUTE)
                    .addAttributes("img", ATTRIBUTE)
                    .addAttributes("iframe", ATTRIBUTE, "allowfullscreen", "src", "frameborder");

    private HtmlUtils() {
    }

    /**
     * This method is used to validate the description of challenges and tasks. Use {@code addTags} and
     * {@code addAttributes} on {@code Safelist} class to add new tags and attributes to whitelist. And don't forget to
     * update error message. Stops if desc is null.
     *
     * @param desc put html code to validate
     * @throws IncorrectInputException throws if the code has forbidden tags and attributes
     * @see Safelist
     */
    public static void validateDescription(String desc) {
        if (desc == null) {
            return;
        }
        log.info(desc);
        log.debug(Jsoup.clean(desc, DESC_SAFELIST));
        if (!Jsoup.isValid(desc, DESC_SAFELIST)) {
            throw new IncorrectInputException(FORBIDDEN_DESC_TAGS);
        }
    }
}
