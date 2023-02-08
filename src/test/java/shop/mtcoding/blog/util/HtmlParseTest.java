package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class HtmlParseTest {

    @Test
    public void jsoup_test1() throws Exception {
        System.out.println("==========================================");
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc.title());
        System.out.println("==========================================");
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
            System.out.println(headline.absUrl("href"));
            System.out.println("------------------------------------------");
        }
    }

    @Test
    public void jsoup_test2() throws Exception {
        String html = "<p>1</p><p><script src=\"\"></script><img id=\"img1\" src=\"data:image/png;base64,iVBORw0KG\"><img id=\"img2\" src=\"data:image/jpeg;base64,iVBORw0KGaefd\"></p><p><b>1</b></p>";
        Document doc = Jsoup.parse(html);
        // System.out.println(doc);
        Elements els = doc.select("img");
        // System.out.println(els);
        if (els.size() == 0) {
            // 임시 사진 제공
            // 디비 thumbnail 에다가 /images/profile.jpg
        } else {
            Element el = els.get(0);
            String img = el.attr("src");
            System.out.println(img);
        }
        // insert, update
    }

    @Test
    public void jsoup_test3() throws Exception {
        Document doc = Jsoup.connect("https://comic.naver.com/webtoon/weekdayList?week=wed").get();
        System.out.println(doc.title());

        Elements tag = doc.select("img");
        for (Element element : tag) {
            String attr = element.attr("src");
            System.out.println(attr);
        }
    }

    @Test
    public void parse_test1() {
        // given
        String html = "<p>1</p><p><script src=\"\"></script><img id=\"img1\" src=\"data:image/png;base64,iVBORw0KG\"></p><p><b>1</b></p>";

        // when
        // boolean b = html.contains("img src");
        // if (b) {
        // System.out.println("테스트 : 사진 있음");
        // } else {
        // System.out.println("테스트 : 사진 없음 ");
        // }
        String tag = parseEL(html, "img");
        System.out.println(tag);
        String attr = parseAttr(tag, "src");
        System.out.println(attr);
    }

    private String parseEL(String html, String tag) {
        String s1 = html.substring(html.indexOf(tag) - 1);
        return s1.substring(0, s1.indexOf(">") + 1);
    }

    private String parseAttr(String el, String attr) {
        String s1 = el.substring(el.indexOf(attr));

        int begin = s1.indexOf("\"");
        int end = s1.indexOf("\"", 2);

        return s1.substring(begin + 1, end);
    }

}
