package com.guocanjie.controller;

import com.guocanjie.aop.cache.CacheAnnotation;
import com.guocanjie.aop.log.LogAnnotation;
import com.guocanjie.service.ArticleService;
import com.guocanjie.utils.ArticleParams;
import com.guocanjie.utils.PageParams;
import com.guocanjie.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    /**
     * 首页  文章列表
     * @param pageParams
     * @return
     */
//    为该接口添加一个自定义注解
    @LogAnnotation(module = "文章模块", operation = "获取文章列表")
    @PostMapping
    public Request getPage(@RequestBody PageParams pageParams){
        return articleService.getPage(pageParams);
    }

    /**
     * 最热门文章
     * @return
     */
    @PostMapping("/hot")
    @CacheAnnotation(expir = 10 * 60 * 1000, name = "getHotArticle")
    public Request getHotArticle(){
        int limit = 5;
        return articleService.getHotArticle(limit);
    }

    /**
     * 最新文章
     * @return
     */
    @PostMapping("/new")
    public Request getNewArticle(){
        int limit = 5;
        return articleService.getNewArticle(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("/listArchives")
    public Request getArchives(){
        int limit = 2;
        return articleService.getArchives(limit);
    }

    @PostMapping("/view/{id}")
    public Request getArticleView(@PathVariable Long id){
        return articleService.getArticleBody(id);
    }

    /**
     * 请求参数
     * {
     *     "title":"大奉打更人",
     *     "body":{"content":"大奉打更人内容","contentHtml":"大奉打更人内容HTML"},
     *     "category":{"id": "2", "avatar": "/category/back.png", "categoryName": "后端"},
     *     "summary":"小说",
     *     "tags":[{"id": "5"},{"id": "6"}]
     * }
     * @param articleParams
     * @return
     */
    @PostMapping("/publish")
    public Request pushArticle(@RequestBody ArticleParams articleParams){
        return articleService.pushArticle(articleParams);
    }

}
