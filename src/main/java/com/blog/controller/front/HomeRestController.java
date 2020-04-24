package com.blog.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.constants.CacheKey;
import com.blog.common.utils.CacheUtil;
import com.blog.controller.base.BaseController;
import com.blog.model.bean.R;
import com.blog.model.entity.Article;
import com.blog.model.entity.Channel;
import com.blog.model.entity.Label;
import com.blog.model.vo.ArticleVO;
import com.blog.service.ArticleDetailService;
import com.blog.service.ArticleService;
import com.blog.service.ChannelService;
import com.blog.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页控制器
 *
 * @author avalon
 * @date 2019/4/19
 */
@RequestMapping(value = "/home/article")
@RestController
@Slf4j
public class HomeRestController extends BaseController {

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
    public R homePre(@RequestParam(defaultValue = "1") int page,
                     @RequestParam(defaultValue = "10") int size, @RequestParam(value = "title", defaultValue = "") String title, @RequestParam(value = "labels", defaultValue = "") String labels) {
        ArticleVO articleVO = new ArticleVO();
        articleVO.setTitle(title);
        articleVO.setLabels(labels);
        articleVO.setSort(" a.update_date desc ");
        Page<ArticleVO> pages =
                articleService.pageFrontArticleVO(new Page<ArticleVO>(page, size), articleVO);
        return R.page2(pages);
    }

    /**
     * 排行榜
     *
     * @return
     */
    @GetMapping("/isView")
    public R isView() {
        ArticleVO articleVO = new ArticleVO();
        articleVO.setSort(" ad.view_count desc ");
        Page<ArticleVO> pages = articleService.pageFrontArticleVO(new Page<ArticleVO>(1, 5), articleVO);
        return R.page2(pages);
    }

    /**
     * 排行榜
     *
     * @return
     */
    @GetMapping("/isUp")
    public R isUp() {
        ArticleVO articleVO = new ArticleVO();
        articleVO.setSort("  ad.up_count desc ");
        Page<ArticleVO> pages = articleService.pageFrontArticleVO(new Page<ArticleVO>(1, 5), articleVO);
        return R.page2(pages);
    }

    /**
     * 标签云(详细描述)
     *
     * @return
     */
    @GetMapping("/labels")
    public R pageLabels() {

        Object labelsObj = cacheUtil.get(CacheUtil.TWENTYFOUR_HOURS, CacheKey.CACHE_LABELS_KEY);
        List<Label> list = null;

        if (labelsObj == null || StringUtils.isBlank(labelsObj.toString())) {
            list = labelService.list();
            cacheUtil.set(CacheUtil.TWENTYFOUR_HOURS, CacheKey.CACHE_LABELS_KEY, JSONArray.fromObject(list).toString());
        } else {
            list = JSONArray.fromObject(labelsObj);
        }

        return R.okT(list);
    }


    /**
     * 标签云(大类型)
     *
     * @return
     */
    @GetMapping("/channels")
    public R channels() {

        return R.okT(channelService.list());
    }


    /**
     * 时间轴
     *
     * @return
     */
    @GetMapping("/time")
    public R time(@RequestParam(defaultValue = "1") int page,
                  @RequestParam(defaultValue = "13") int size) {
        Page<ArticleVO> pages = articleService.pageFrontArticleVO(new Page<ArticleVO>(page, size), null);
        return R.page2(pages);
    }


    /**
     * 文章详细
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable Long id) {
        //添加浏览记录
        articleDetailService.updateViewCount(Arrays.asList(id));
        return R.okT(articleService.findById(id));
    }


    /**
     * 添加点赞记录
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public R upCount(@PathVariable(value = "id") Long id) {
        //添加点赞记录
        return articleDetailService.updateUpCount(Arrays.asList(id));
    }


    /**
     * 获取所有的文章标题
     *
     * @return
     */
    @GetMapping("/titles")
    public R titles(){
        return R.okT(articleService.list().stream().map(Article::getTitle).collect(Collectors.toList()));
    }
}
