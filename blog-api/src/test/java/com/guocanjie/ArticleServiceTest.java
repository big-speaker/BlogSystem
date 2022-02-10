package com.guocanjie;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.ArticleMapper;
import com.guocanjie.dao.pojo.Article;
import com.guocanjie.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BlogApplication.class)
public class ArticleServiceTest {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleService articleService;

    @Test
    public void Test01(){
        System.out.println("articleService结果输出:"+articleService.getHotArticle(5));
    }

    @Test
    public void Test02(){
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        设置article按照getViewCounts排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
//        设置查询列为getId和getTitle
        lambdaQueryWrapper.select(Article::getId, Article::getTitle);
        System.out.println("articleMapper结果输出:"+articleMapper.selectList(lambdaQueryWrapper));
    }

    @Test
    public void Test03(){
        System.out.println("结果输出++++++++"+articleService.getArticleBody(1L));
    }


}
