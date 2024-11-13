package kiss.depot.model.bo.pageBo;

import kiss.depot.config.exceptionConfig.exceptions.CommonErrException;
import kiss.depot.config.exceptionConfig.exceptions.ParamCheckException;
import kiss.depot.model.constant.STATIC;
import kiss.depot.model.enums.CommonErr;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
 * 分页查询业务逻辑对象（无页码）
 * 一般出现在下拉获取新帖子的场景，这种查询方式无需全表检索并返回总数据量
 * 如果表中有主键字段顺序字段，我们可以通过这个字段来略过大量数据，进一步提升搜索效率
 * author: koishikiss
 * launch: 2024/11/3
 * last update: 2024/11/5
 * */

@Getter
public class SinglePageSearchBO<T, K> {  // T:返回数据列表中的数据类型  K:记录位置信息的数据的数据类型

    @Setter
    private K firstData;  //查询结果队列的首条数据的位置信息(用户可定义)

    @Setter
    private K lastData;  //查询结果队列的尾条数据的位置信息(用户可定义)

    private Integer pageSize;  //每页数据条数(用户可定义)

    @Setter
    private DIRECTION direction;  //查询方向（true向前/向后）(用户可定义)

    private List<T> dataList;  //得到的数据列表

    //设置默认参数
    public SinglePageSearchBO() {
        pageSize = STATIC.VALUE.page_size;
        direction = DIRECTION.BACK;
    }

    //控制页大小
    @SuppressWarnings("unused")
    public void setPageSize(Integer pageSize) {
        if (pageSize < STATIC.VALUE.min_page_size) {
            throw new ParamCheckException("页面大小设置过小！");
        } else if (STATIC.VALUE.max_page_size < pageSize) {
            throw new ParamCheckException("页面大小设置过大！");
        } else {
            this.pageSize = pageSize;
        }
    }

    public List<T> doSearch(Method<T, K> method) {
        //如果向前搜索
        if (direction.equals(DIRECTION.FRONT)) {
            //如果传来的第一个数据为null，也就是首次搜索的状况，那么理论上不会有更新的数据
            if (firstData == null) throw new CommonErrException(CommonErr.THIS_IS_FIRST_PAGE.setMsg("已经是最新了哦"));

            //从firstData的位置开始向前查询
            dataList = method.searchTowardFront(firstData, pageSize);

            //向前搜索无结果，表明已经向前搜索到头
            if (dataList.isEmpty()) throw new CommonErrException(CommonErr.THIS_IS_FIRST_PAGE.setMsg("已经是最新了哦"));

            //否则更新一下首条数据
            else firstData = method.updateFirstData(dataList);
        }
        //如果向后搜索且后一个数据为null，则表明这是第一次搜索（进入页面时的搜索）
        else if (lastData == null) {
            //发起首次搜索
            dataList = method.firstSearch(pageSize);

            //首次搜索无结果，表明无数据
            if (dataList.isEmpty()) throw new CommonErrException(CommonErr.NO_DATA);

            //否则将首条数据和尾条数据都更新一下
            else {
                firstData = method.updateFirstDataAtFirstSearch(dataList);
                lastData = method.updateLastDataAtFirstSearch(dataList);
            }
        }
        //否则
        else {
            //从lastData位置开始向后搜索
            dataList = method.searchTowardBack(lastData, pageSize);

            //向后搜索无结果，表明已经向后搜索到头
            if (dataList.isEmpty()) throw new CommonErrException(CommonErr.THIS_IS_LAST_PAGE.setMsg("已经翻到最后了哦"));

            //否则更新一下尾条数据
            else lastData = method.updateLastData(dataList);
        }

        //一切正常，返回分页结果dataList
        return dataList;
    }

    //得到查询结果中返回列表的某一项，仅在搜索结束后允许调用
    public T getResultItem(int i) {
        i = i < 0 ? i + dataList.size() : i;
        return dataList.get(i);
    }

    public interface Method<T, K> {
        //首次搜索
        List<T> firstSearch(Integer pageSize);

        //首次搜索后更新首条数据
        default K updateFirstDataAtFirstSearch(List<T> resultList) {
            return updateFirstData(resultList);
        }

        //首次搜索后更新尾条数据
        default K updateLastDataAtFirstSearch(List<T> resultList) {
            return updateLastData(resultList);
        }

        //向前搜索
        List<T> searchTowardFront(K startPos, Integer pageSize);

        //向前搜索结束后更新首条数据，可以交给前端处理，因此这里使用default
        default K updateFirstData(List<T> resultList) {
            return null;
        }

        //向后搜索
        List<T> searchTowardBack(K startPos, Integer pageSize);

        //向后搜索结束后更新尾条数据，可以交给前端处理，因此这里使用default
        default K updateLastData(List<T> resultList) {
            return null;
        }
    }

    public enum DIRECTION {
        FRONT,  //向前搜索
        BACK  //向后搜索
    }

}
