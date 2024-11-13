package kiss.depot.model.bo.pageBo;

import kiss.depot.config.exceptionConfig.exceptions.CommonErrException;
import kiss.depot.config.exceptionConfig.exceptions.ParamCheckException;
import kiss.depot.model.constant.STATIC;
import kiss.depot.model.enums.CommonErr;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
* 分页查询业务逻辑对象（半页码）
* author: koishikiss
* launch: 2024/11/5
* last update: 2024/11/5
* */

@Getter
public class MorePageSearchBO<T> {

    @Setter
    private Integer pagination;  //页码(用户可定义)

    private Integer pageSize;  //每页数据条数(用户可定义)

    private Integer offset;  //略过数据条数(用户可定义)

    private Integer scanPage;  //往后扫描的页数(用户可定义)

    private Integer restPage;  //实际剩余的页面

    private List<T> dataList;  //得到的数据列表

    //设置默认参数
    public MorePageSearchBO() {
        pagination = 1;
        pageSize = STATIC.VALUE.page_size;
        scanPage = STATIC.VALUE.scan_page_size;
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

    //控制offset大小
    @SuppressWarnings("unused")
    public void setOffset(Integer offset) {
        if (offset < 0) {
            throw new CommonErrException(CommonErr.PARAM_WRONG);
        } else {
            this.offset = offset;
        }
    }

    //控制scanPage大小
    @SuppressWarnings("unused")
    public void setScanPage(Integer scanPage) {
        if (scanPage < STATIC.VALUE.min_scan_page_size) {
            throw new ParamCheckException("展示页数设置过小！");
        } else if (STATIC.VALUE.max_scan_page_size < scanPage) {
            throw new ParamCheckException("展示页数设置过大！");
        } else {
            this.scanPage = scanPage;
        }
    }

    //通过页码和页大小计算略过数据条数
    private void calculateOffset() {
        if (pagination < 1) throw new CommonErrException(CommonErr.THIS_IS_FIRST_PAGE);
        offset = offset == null ? (pagination - 1) * pageSize : (pagination - 1) * pageSize + offset;
    }

    //通过往后扫描的页数和当前页计算出要扫描的数据量
    private Integer calculateScanSize() {
        return pageSize * scanPage + offset + 1;
    }

    //根据数据总条数和页大小计算总页数
    private void calculatePageNum(Integer dataNum) {
        Integer totalPage = dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
        if (pagination > totalPage) throw new CommonErrException(CommonErr.THIS_IS_LAST_PAGE);
        else restPage = totalPage - pagination;
    }

    //进行分页查询
    public List<T> doResearch(Method<T> method) {

        //计算出offset
        calculateOffset();

        //计算需要扫描的数据限制下能查找到的数据条数
        Integer dataNum = method.getDataNumWithinLimit(calculateScanSize());

        //这里可以根据前端需要抛出无数据异常或返回空记录
        if (dataNum <= 0) throw new CommonErrException(CommonErr.NO_DATA);

        //计算实际剩余页面数
        calculatePageNum(dataNum);

        //查询数据
        dataList = method.getData(offset,pageSize);

        //查找数据的同时发生了删除数据操作，便可能导致空列表
        if (dataList.isEmpty()) throw new CommonErrException(CommonErr.RESOURCE_NOT_FOUND);

        //最后返回dataList
        return dataList;
    }

    //得到查询结果中返回列表的某一项，仅在搜索结束后允许调用
    public T getResultItem(int i) {
        i = i < 0 ? i + dataList.size() : i;
        return dataList.get(i);
    }

    public interface Method<T> {

        //需要一个查询数据量的接口
        Integer getDataNumWithinLimit(Integer scanSize);

        //需要一个查找当前页数据的接口
        List<T> getData(Integer offset, Integer pageSize);
    }


}
