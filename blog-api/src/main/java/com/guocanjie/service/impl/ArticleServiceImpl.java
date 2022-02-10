package com.guocanjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guocanjie.dao.mapper.ArticleBodyMapper;
import com.guocanjie.dao.mapper.ArticleMapper;
import com.guocanjie.dao.mapper.ArticleTagMapper;
import com.guocanjie.dao.pojo.*;
import com.guocanjie.service.*;
import com.guocanjie.utils.ArticleParams;
import com.guocanjie.utils.PageParams;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.UserThreadLocal;
import com.guocanjie.utils.vo.ArticleBodyVo;
import com.guocanjie.utils.vo.ArticleVo;
import com.guocanjie.utils.vo.CategoryVo;
import com.guocanjie.utils.vo.TagVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleBodyService articleBodyService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Override
    public Request getPage(PageParams pageParams) {
//        设置分页
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        设置查询条件
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
//        按getWeight和getCreateDate排序(倒序)
        lqw.orderByDesc(Article::getWeight);
//        lqw.orderByDesc(Article::getCreateDate);

//        如果参数有categoryId,则按categoryId查询
        if(pageParams.getCategoryId() != null){
            lqw.eq(Article::getCategoryId,pageParams.getCategoryId());
        }

//        如果参数有tagId,则查出所有tagId标签的文章
        List<Long> idList = new ArrayList<>();
        if(pageParams.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            if(articleTags != null){
                for (ArticleTag articleTag : articleTags) {
                    idList.add(articleTag.getArticleId());
                }
            }
            lqw.in(Article::getId,idList);
        }

//        用MyBatis-plus内置分页查询API,将查询结果赋值给分页对象
        Page<Article> articlePage = articleMapper.selectPage(page, lqw);
//        获取分页对象中的分页数据
        List<Article> records = articlePage.getRecords();

        List<ArticleVo> articleVoList = copyList(records);
        return new Request(articleVoList);
    }

    @Override
    public Request getHotArticle(int Limit) {
//        创建查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        设置article按照getViewCounts排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
//        设置查询列为getId和getTitle
//        lambdaQueryWrapper.select(Article::getId, Article::getTitle);
        List<Article> hotArticleList = articleMapper.selectList(lambdaQueryWrapper);
        return new Request(copyList(hotArticleList));
    }

    @Override
    public Request getNewArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
//        lambdaQueryWrapper.select(Article::getId, Article::getTitle);
        List<Article> newArticleList = articleMapper.selectList(lambdaQueryWrapper);
        return new Request(copyList(newArticleList));
    }

    @Override
    public Request getArchives(int limit) {
        List<Archives> archives = articleMapper.getArchives(limit);
        return new Request(archives);
    }

    @Override
    public Request getArticleBody(Long articleId) {
        /**
         * 1 通过articleId查询article
         * 2 通过articleId查询category列表
         * 3 通过articleId查询articleBody
         */
        Article article = articleMapper.selectById(articleId);
//        List<CategoryVo> categoryList = categoryService.getCategoryById(articleId);
//        ArticleBodyVo articleBody = articleBodyService.getArticleBodyById(articleId);
        ArticleVo articleVo = copy(article);
//        articleVo.setCategorys(categoryList);
//        articleVo.setBody(articleBody);
//        运用线程吃更改文章观看数
        threadService.updateArticleViewCount(articleMapper, article);
        return new Request(articleVo);
    }

    @Override
    public Request pushArticle(ArticleParams articleParams) {
        /**
         * 1、获取文章信息，将文章数据插入数据库
         * 2 判断articleParams参数是否有TagoVo参数,如果有,将插入数据库的article生成的id与tag的id插入article_tag表
         * 3 将Article的内容插入articleBody表中,并获取bodyId
         * 4 判断articleParams参数是否有CategoryVo参数,如果有,更新Article的CategoryId参数
         * 5 更新Article的BodyId参数,更新Article
         * 6 创建一个HashMap集合,返回Article的id
         */
//        新建一个文章对象
        Article article = new Article();
//        利用线程获取用户信息
        SysUser sysUser = UserThreadLocal.get();
//        向文章对象导入对象数据
        article.setSummary(articleParams.getSummary());
        article.setTitle(articleParams.getTitle());
        article.setCreateDate(System.currentTimeMillis());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setAuthorId(sysUser.getId());
//        将文章对象插入数据库 插入成功后会生成一个ID
        articleMapper.insert(article);

//        判断tag是否为空,不为空则将tag和article的id插入article_tag表中
        List<TagVo> tagVoList = new ArrayList<>();
        if(articleParams.getTags() != null){
            for (TagVo tagVo : articleParams.getTags()) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tagVo.getId());
                articleTagMapper.insert(articleTag);
            }
        }

//        将Article的内容插入ArticleBody表中
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParams.getBody().getContent());
        articleBody.setContentHtml(articleParams.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

//        判断是否有CategoryId参数
        if(articleParams.getCategory() != null){
            article.setCategoryId(articleParams.getCategory().getId());
        }

//        跟新Article的BodyId参数
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

//        创建一个HashMap集合返回id
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return new Request(map);
    }


    /**
     * 将Article列表通过循环转换成ArticleVo列表
     * @param articleList Article列表
     * @return  返回ArticleVo列表
     */
    public List<ArticleVo> copyList(List<Article> articleList){
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : articleList) {
            articleVoList.add(copy(article));
        }
        return articleVoList;
    }

    /**
     * 将Article对象转换为ArticleVo对象
     * @param article Article对象
     * @return  返回ArticleVo对象
     */
    public ArticleVo copy(Article article){
        ArticleVo articleVo = new ArticleVo();

//        BeanUtils对象的copyProperties方法可以将Article对象中与ArticleVo对象中的相同的成员变量复制到ArticleVo对象
        BeanUtils.copyProperties(article,articleVo);
//        获取Article中的createDate将其转换成"yyyy-mm-ss  HH"格式并赋值给ArticleVo对象
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-mm-ss  HH"));

//        如果有Tag,将Tag存入articleVo对象中
        if(!CollectionUtils.isEmpty(tagService.getTag(article.getId()))){
            List<TagVo> tagVoList = tagService.getTag(article.getId());
            articleVo.setTags(tagVoList);
        }

//        如果有Autor,将Autor写入ArticleVo对象中
        if(sysUserService.getSysUser(article.getAuthorId()) != null){
            String nickname = sysUserService.getSysUser(article.getAuthorId()).getNickname();
            articleVo.setAuthor(nickname);
        }

//        如果有Category,将category写入ArticleVo对象中
        if(categoryService.getCategoryById(article.getCategoryId()) != null){
            CategoryVo categoryById = categoryService.getCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryById);
        }

//        如果有BodyId,将ArticleBodyVo写入ArticleVo对象中
        if(articleBodyService.getArticleBodyById(article.getBodyId()) != null){
            ArticleBodyVo articleBodyById = articleBodyService.getArticleBodyById(article.getBodyId());
            articleVo.setBody(articleBodyById);
        }

        return articleVo;
    }

}
