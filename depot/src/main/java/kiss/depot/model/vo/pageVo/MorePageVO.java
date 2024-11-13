package kiss.depot.model.vo.pageVo;

/*
* 多分页VO，根据前端需求调整
* author: koishikiss
* launch: 2024/11/5
* last update: 2024/11/5
* */

import kiss.depot.model.bo.pageBo.MorePageSearchBO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MorePageVO<T> {

    private Integer restPage;  //实际剩余的页面

    private Integer pagination;  //当前页码

    private Integer firstRecordNum;  //返回数据列表的首条数据位置

    protected List<T> records;  //新得到的数据列表

    public MorePageVO(MorePageSearchBO<T> searchResult) {
        restPage = searchResult.getRestPage();
        pagination = searchResult.getPagination();
        firstRecordNum = searchResult.getOffset() + 1;
        records = searchResult.getDataList();
    }
}
