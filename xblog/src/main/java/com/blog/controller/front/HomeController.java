package com.blog.controller.front;

import com.blog.controller.common.BaseController;
import com.blog.model.bean.ResultData;
import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Article;
import com.blog.service.ArticleDetailService;
import com.blog.service.ArticleService;
import com.blog.service.ChannelService;
import com.blog.service.LabelService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页控制器
 *
 * @author avalon
 * @date 2019/4/19
 */
@RequestMapping(value = "/home")
@RestController
@Slf4j
public class HomeController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    protected ArticleDetailService articleDetailService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping
    public ResultData homePre(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size, ArticleDTO articleDTO) {
        PageRequest pageRequest = PageRequest.of(page, size);
        ResultData r = articleService.page(pageRequest, articleDTO);
        return r;
    }

    /**
     * 标签云(详细描述)
     *
     * @return
     */
    @GetMapping("/labels")
    public ResultData pageLabels() {

        return labelService.list();
    }


    /**
     * 标签云(大类型)
     *
     * @return
     */
    @GetMapping("/channels")
    public ResultData channels() {

        return channelService.list();
    }


    /**
     * 时间轴
     *
     * @return
     */
    @GetMapping("/time")
    public ResultData time(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "13") int size) {

        return articleService.page(PageRequest.of(page, size), null);
    }


    /**
     * 文章详细
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultData detail(@PathVariable Long id) {
        //添加浏览记录
        articleDetailService.updateViewCount(Arrays.asList(id));
        return ResultData.data(articleService.findById(id));
    }


    /**
     * 添加点赞记录
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public ResultData upCount(@PathVariable(value = "id") Long id) {
        //添加点赞记录
        return articleDetailService.updateUpCount(Arrays.asList(id));
    }


    /**
     * 获取所有的文章标题
     *
     * @return
     */
    @GetMapping("/titles")
    public ResultData titles() {
        ResultData<List<Article>> r = articleService.list();

        if (r.isOk()) {
            return ResultData.data(r.getData().stream().map(Article::getTitle).collect(Collectors.toList()));
        }

        return ResultData.ok();
    }


    @Data
    public static class CurrUser {
        private String name = "郑新";
        private String username = "Avalon";
        private String email = "694948443@qq.com";
        private String wechat = "slucky694948443";
        private String phone = "13008891817";
        private String qq = "694948443";
        private String description = "书中自有颜如玉,书中自有黄金屋";
        private String job = "Java工程师";

    }

    public static CurrUser currUser = new CurrUser();


    @GetMapping("/curr")
    public ResultData currUser() {
        return ResultData.data(currUser);
    }
}
