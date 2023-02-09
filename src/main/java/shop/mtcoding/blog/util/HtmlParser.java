package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

    public static String getThumbnail(String html) {
        String thumbnail = "";
        Document doc = Jsoup.parse(html);
        // System.out.println(doc);
        Elements els = doc.select("img");
        // System.out.println(els);

        if (els.size() == 0) {
            thumbnail = "/images/dora.png";
            // 디비 thumbnail 에다가 /images/dora.png
        } else {
            Element el = els.get(0);
            thumbnail = el.attr("src");
        }

        return thumbnail;
    }
}
