package com.guocanjie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.ArticleMapper;
import com.guocanjie.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadService {

    /**
     * 利用多线程更新文章阅读数量
     * @param articleMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){

        Article articleUpdate = new Article();
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getId, article.getId());
        lambdaQueryWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleUpdate.setViewCounts(article.getViewCounts() + 1);
        articleMapper.update(articleUpdate,lambdaQueryWrapper);
    }
}
