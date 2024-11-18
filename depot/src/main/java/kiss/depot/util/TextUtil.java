package kiss.depot.util;

/*
* 文本装饰修改工具类
* author: koishikiss
* launch: 2024/11/18
* last update: 2024/11/18
* */

public class TextUtil {

    private static final String truncatedTail = "...";

    //截取字数，超出范围则将wordsSize位置的字符替换为truncatedTail，后面的字符省略
    public static String truncatedWords(int wordsSize, String text) {
        if (text.length() >= wordsSize) {
            return text.substring(0,wordsSize-1) + truncatedTail;
        } else {
            return text;
        }
    }

    //截取行数，超出范围则将linesSize位置的字符替换为truncatedTail，后面的行省略
    public static String truncatedLines(int linesSize, String text) {
        String[] lines = text.split("\n");
        if (lines.length >= linesSize) {
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < linesSize-1; i++) {
                newText.append(lines[i]).append('\n');
            }
            newText.append(truncatedTail);
            return newText.toString();
        } else {
            return text;
        }
    }

    //截取行数和字数，优先截取行数，行数在范围内的情况下截取字数
    public static String truncatedLinesAndWords(int linesSize, int wordsSize, String text) {
        String[] lines = text.split("\n");
        if (lines.length >= linesSize) {
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < linesSize-1; i++) {
                newText.append(lines[i]).append('\n');
            }
            newText.append(truncatedTail);
            return newText.toString();
        } else if (text.length() >= wordsSize) {
            return text.substring(0,wordsSize-1) + truncatedTail;
        } else {
            return text;
        }
    }
}
