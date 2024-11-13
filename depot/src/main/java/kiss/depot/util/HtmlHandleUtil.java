package kiss.depot.util;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.util.HtmlUtils;

/*
* HTML文本处理工具类
* HtmlUtils的简单封装以及一些其它相关于HTML文本的操作
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

public class HtmlHandleUtil {

    private HtmlHandleUtil() {}

    public static final String unsafeElement = "script,iframe,form,svg,math";
    public static final String fileElement = "img,video,audio";

    @AllArgsConstructor
    public enum HTML_TYPE {
        CONTENT("文章",10000),CONTACT_WAY("联系方式描述",500);
        final String name;
        final int maxLength;
    }

    public static String checkHTML(String content,HTML_TYPE htmlType) {
        if (content == null || content.isBlank()) return "内容不可为空!";

        Document htmlDocument;
        try {
            htmlDocument = Jsoup.parse(content);
        } catch (RuntimeException e) {
            return "文章编辑格式存在错误!";
        }

        //检查是否有危险的标签存在
        if (htmlDocument.select(unsafeElement).toArray().length > 0) {
            return "包含非法的文本!";
        }

        // 删除所有的 <文件> 标签
        for (Element element : htmlDocument.select(fileElement)) {
            element.remove();
        }

        // 获取所有文本
        String text = htmlDocument.body().text();

        // 计算文字数量
        if (text.length() > htmlType.maxLength) {
            return htmlType.name+"不可超过"+htmlType.maxLength+"字!";
        }

        return null;
    }

    //由HTML转换为text
    public static String escapeFromHTML(String html) {
        return HtmlUtils.htmlEscape(html);
    }

    //由text转换为HTML
    public static String escapeToHTML(String text) {
        return HtmlUtils.htmlUnescape(text);
    }

}


/* using dependencies:
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--Jsoup依赖 解析HTML页面-->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.15.3</version>
</dependency>
*/
