package kiss.depot.model.vo.pageVo;

import kiss.depot.model.bo.pageBo.AllPageSearchBO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
* 全分页VO，根据前端需求调整
* author: koishikiss
* launch: 2024/11/4
* last update: 2024/11/4
* */

@Getter
@AllArgsConstructor
public class AllPageVO<T> {

    private Integer maxPagination;  //最大页码数

    private Integer totalRecordNum;  //数据总条数

    private Integer pagination;  //当前页码

    private Integer firstRecordNum;  //首条数据位置

    private List<T> records;  //得到的数据

    public AllPageVO(AllPageSearchBO<T> searchResult) {
        maxPagination = searchResult.getTotalPage();
        totalRecordNum = searchResult.getDataNum();
        pagination = searchResult.getPagination();
        firstRecordNum = searchResult.getOffset() + 1;
        records = searchResult.getDataList();
    }
}
