package kiss.depot.controller;

import jakarta.annotation.Resource;
import kiss.depot.model.bo.pageBo.AllPageSearchBO;
import kiss.depot.model.bo.pageBo.MorePageSearchBO;
import kiss.depot.model.bo.pageBo.SinglePageSearchBO;
import kiss.depot.model.po.Post;
import kiss.depot.model.vo.response.Response;
import kiss.depot.service.TestPageService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * 分页测试相关
 * author: koishikiss
 * launch: 2024/11/4
 * last update: 2024/11/5
 * */

@RestController
public class TestPageController {

    @Resource
    TestPageService testPageService;

    @PostMapping("/test/insertData")
    public Response insertDataIntoDepotPost() {
        return testPageService.insertDataIntoDepotPost();
    }

    @DeleteMapping("/test/randomDeleteData")
    public Response randomDeleteData() {
        return testPageService.randomDeletePost();
    }

    @PostMapping("/test/allPageSearch")
    public Response testAllPageSearch(@RequestBody AllPageSearchBO<Post> allPageSearch) {
        return testPageService.testAllPageSearch(allPageSearch);
    }

    @PostMapping("/test/singlePageSearch")
    public Response testSinglePageSearch(@RequestBody SinglePageSearchBO<Post,Integer> singlePageSearch) {
        return testPageService.testSinglePageSearch(singlePageSearch);
    }

    @PostMapping("/test/morePageSearch")
    public Response testMorePageSearch(@RequestBody MorePageSearchBO<Post> morePageSearch) {
        return testPageService.testMorePageSearch(morePageSearch);
    }

}
