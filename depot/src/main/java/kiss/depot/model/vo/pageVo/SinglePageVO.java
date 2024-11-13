package kiss.depot.model.vo.pageVo;

import kiss.depot.model.bo.pageBo.SinglePageSearchBO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
* 单分页VO，根据前端需求调整
* author: koishikiss
* launch: 2024/11/5
* last update: 2024/11/5
* */

@Getter
@AllArgsConstructor
public class SinglePageVO<T, K> {

    private K firstData;  //查询结果队列的首条数据的位置信息

    private K lastData;  //查询结果队列的尾条数据的位置信息

    protected List<T> records;  //新得到的数据列表

    public SinglePageVO(SinglePageSearchBO<T,K> searchResult) {
        firstData = searchResult.getFirstData();
        lastData = searchResult.getLastData();
        records = searchResult.getDataList();
    }

}
