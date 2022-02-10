package com.guocanjie.service;

import com.guocanjie.utils.ArticleParams;
import com.guocanjie.utils.PageParams;
import com.guocanjie.utils.Request;

import java.util.List;

public interface ArticleService {

//    查询首页文章
    public Request getPage(PageParams pageParams);

//    查询最热文章
    public Request getHotArticle(int Limit);

//    查询最新文章
    public Request getNewArticle(int limit);

//    文章归档
    public Request getArchives(int limit);

//    查询文章详情
    public Request getArticleBody(Long articleId);

//    发表文章
    public Request pushArticle(ArticleParams articleParams);

}
