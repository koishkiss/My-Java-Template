package kiss.depot.model.bo.pageBo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import kiss.depot.config.exceptionConfig.exceptions.CommonErrException;
import kiss.depot.config.exceptionConfig.exceptions.ParamCheckException;
import kiss.depot.model.constant.STATIC.VALUE;
import kiss.depot.model.enums.CommonErr;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
* 分页查询业务逻辑对象（全页码）
* mybatis-plus提供的分页查询插件集成在其单表查询的体系里，当设计到复杂业务时需要解耦，这个插件就不好用了
* 使用自建的极其简单的分页帮助对象可以更清晰地控制我们的业务
* author: koishikiss
* launch: 2024/11/3
* last update: 2024/11/5
* */

@Getter
public class AllPageSearchBO<T> {  // T:返回数据列表中的数据类型

    private Integer totalPage;  //总页数

    @Setter
    private Integer pagination;  //页码(用户可定义)

    private Integer pageSize;  //每页数据条数(用户可定义)

    private Integer dataNum;  //数据总条数(用户可定义)

    private Integer offset;  //略过数据条数(用户可定义)

    private List<T> dataList;  //得到的数据列表

    //设置默认参数
    public AllPageSearchBO() {
        pagination = 1;
        pageSize = VALUE.page_size;
    }

    //控制页大小
    @SuppressWarnings("unused")
    public void setPageSize(Integer pageSize) {
        if (pageSize < VALUE.min_page_size) {
            throw new ParamCheckException("页面大小设置过小！");
        } else if (VALUE.max_page_size < pageSize) {
            throw new ParamCheckException("页面大小设置过大！");
        } else {
            this.pageSize = pageSize;
        }
    }

    //控制dataNum大小
    @SuppressWarnings("unused")
    public void setDataNum(Integer dataNum) {
        if (dataNum <= 0) {
            throw new CommonErrException(CommonErr.PARAM_WRONG);
        } else {
            this.dataNum = dataNum;
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

    //通过页码和页大小计算略过数据条数
    private void calculateOffset() {
        if (pagination < 1) throw new CommonErrException(CommonErr.THIS_IS_FIRST_PAGE);
        offset = offset == null ? (pagination - 1) * pageSize : (pagination - 1) * pageSize + offset;
    }

    //根据数据总条数和页大小计算总页数
    private void calculatePageNum() {
        totalPage = dataNum <= pageSize ? 1 : (dataNum-1) / pageSize + 1;
        if (pagination > totalPage) throw new CommonErrException(CommonErr.THIS_IS_LAST_PAGE);
    }

    //进行分页查询
    public List<T> doSearch(Method<T> method) {

        //先计算出offset
        calculateOffset();

        //随后获取数据总条数
        if (dataNum == null) {
            dataNum = method.getDataNum();
        }

        //这里可以根据前端需要抛出无数据异常或返回空记录
        if (dataNum <= 0) throw new CommonErrException(CommonErr.NO_DATA);

        //计算总页数
        calculatePageNum();

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

    //转换为mybatis-plus分页查询
    public Page<T> toPageInMybatis() {
        return new Page<>(pagination, pageSize);
    }

    //需要传入的方法
    public interface Method<T> {

        //需要一个查询数据总条数的方法
        Integer getDataNum();

        //需要一个查询数据记录的方法
        List<T> getData(Integer offset, Integer pageSize);

    }

}
