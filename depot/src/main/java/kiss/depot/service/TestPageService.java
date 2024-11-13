package kiss.depot.service;

import kiss.depot.model.bo.pageBo.AllPageSearchBO;
import kiss.depot.model.bo.pageBo.MorePageSearchBO;
import kiss.depot.model.bo.pageBo.SinglePageSearchBO;
import kiss.depot.model.constant.MAPPER;
import kiss.depot.model.po.Post;
import kiss.depot.model.vo.pageVo.AllPageVO;
import kiss.depot.model.vo.pageVo.MorePageVO;
import kiss.depot.model.vo.pageVo.SinglePageVO;
import kiss.depot.model.vo.response.Response;
import kiss.depot.util.ArrayUtil;
import kiss.depot.util.RandomUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestPageService {

    @SneakyThrows
    public Response insertDataIntoDepotPost() {
        List<Post> posts = new ArrayList<>();
        //测试性地插入10条数据
//        for (int i = 0; i < 10; i++) {
//            posts.add(new Post(RandomUtil.generateRandomCode(10)));
//        }
        //大胆性地插入100000条数据，并且断断续续地添加模拟不同时间插入（整个过程将花费大量时间）
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 100; j++) {
                posts.add(new Post(RandomUtil.generateRandomCode(10)));
            }
            MAPPER.post.insertAll(posts);
            posts.clear();
            Thread.sleep(RandomUtil.generateRandomLongNum(1000,5000));
        }
        return Response.ok();
    }

    public Response randomDeletePost() {
        Integer total = MAPPER.post.getMaxId();
        Random random = new Random();
        Set<Integer> idSet = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            idSet.add(random.nextInt(total));
        }
        MAPPER.post.deleteAll(ArrayUtil.listToString(idSet.stream().toList()));
        return Response.ok();
    }

    /*
    * 以下为全页码和无页码的测试
    * 在少量数据的情况下无页码的效率约是全页码的两倍
    * 在大量数据下（5005010条），全页码在查询最后几条数据时用时甚至超过了1秒，在查询前几条数据时也花费了超过100毫秒的时间，而无页码则依然保持2毫秒的惊人记录。
    * 当然，全分页并非不可优化，而凭借索引优势的无页码也并非一直能有这个优势。并且，无页码有不完整性和不易编写性，因此，实际场景中还是根据需求权衡。
    *
    * 11.5更新测试
    * 将半分页查询页加入了测试，结果是在600000条数据内，半分页查询的性能优于全分页查询————
    * 但在这之后半分页查询的效率会大大降低，以至于在查最后一页时，用时超过了3秒，是全分页的三倍！
    * 但这是全分页在主键数量统计优势下实现的，并不是说所有情况下都会是这个结果，而且一般也不会翻到这个页数，所以半分页查询还是有可用之处的。
    * 不过相比下使用主键索引标记位置的无页码分页就相当恐怖了，不管在哪个位置都保持着5ms内的速度。
    * */

    public Response testAllPageSearch(AllPageSearchBO<Post> allPageSearch) {
        long before = System.currentTimeMillis();

        //这里将postList获取下来方便做某些处理，当然无需处理时也可以不取
        List<Post> postList = allPageSearch.doSearch(new AllPageSearchBO.Method<>() {
            @Override
            public Integer getDataNum() {
                return MAPPER.post.count();
            }

            @Override
            public List<Post> getData(Integer offset, Integer pageSize) {
                return MAPPER.post.select(offset,pageSize);
            }
        });

        //将content转换的处理
        for (Post post : postList) {
            post.convertContent();
        }

        System.out.println("全分页用时：" + (System.currentTimeMillis() - before));

        return Response.success(new AllPageVO<>(allPageSearch));
    }

    public Response testSinglePageSearch(SinglePageSearchBO<Post,Integer> singlePageSearch) {
        long before = System.currentTimeMillis();

        //获取查询结果
        //注意如果更新首条数据和更新尾条数据实现方法上完全相同，那么你理应重写updateFirstDataAtFirstSearch方法，否则在获取首条数据时会出现逻辑错误
        //当然如果你将这一切交给前端处理，那么这些方法都不用重写。
        List<Post> postList = singlePageSearch.doSearch(new SinglePageSearchBO.Method<>() {
            @Override
            public List<Post> firstSearch(Integer pageSize) {
                return MAPPER.post.firstSearch(pageSize);
            }

            @Override
            public List<Post> searchTowardFront(Integer startPos, Integer pageSize) {
                List<Post> postList = MAPPER.post.searchTowardFront(startPos,pageSize);
                //根据实际需求决定这里是否需要倒序处理
                Collections.reverse(postList);
                return postList;
            }

            @Override
            public Integer updateFirstData(List<Post> resultList) {
                return resultList.get(0).getId();
            }

            @Override
            public List<Post> searchTowardBack(Integer startPos, Integer pageSize) {
                return MAPPER.post.searchTowardBack(startPos,pageSize);
            }

            @Override
            public Integer updateLastData(List<Post> resultList) {
                return resultList.get(resultList.size() - 1).getId();
            }
        });

        //将content转换的处理
        for (Post post : postList) {
            post.convertContent();
        }

        System.out.println("无页码分页用时：" + (System.currentTimeMillis() - before));

        return Response.success(new SinglePageVO<>(singlePageSearch));
    }

    public Response testMorePageSearch(MorePageSearchBO<Post> morePageSearch) {
        long before = System.currentTimeMillis();

        //获取查询结果
        List<Post> postList = morePageSearch.doResearch(new MorePageSearchBO.Method<>() {
            @Override
            public Integer getDataNumWithinLimit(Integer scanSize) {
                return MAPPER.post.getDataNumWithinLimit(scanSize);
            }

            @Override
            public List<Post> getData(Integer offset, Integer pageSize) {
                return MAPPER.post.select(offset,pageSize);
            }
        });

        //将content转换的处理
        for (Post post : postList) {
            post.convertContent();
        }

        System.out.println("无页码分页用时：" + (System.currentTimeMillis() - before));

        return Response.success(new MorePageVO<>(morePageSearch));

    }

}
