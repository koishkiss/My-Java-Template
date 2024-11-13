package kiss.depot.util;

/*
* 负责计算分页查询相关
* 现在看来应该在一个BO里囊括这些，因此标记过时
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

@Deprecated
public class PageUtil {

    private PageUtil() {}

    public static int calculateOffset(int page, int pageSize) {
        return (page-1) * pageSize;
    }

    public static int calculateOffset(int page, int pageSize, int startOffset) {
        return (page-1) * pageSize + startOffset;
    }

    public static int calculatePageNum(int dataNum, int pageSize) {
        return dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
    }

    public static int calculatePageNum(int dataNum, int pageSize, int startOffset) {
        dataNum -= startOffset;
        return dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
    }

}
