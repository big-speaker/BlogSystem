package com.guocanjie.service.impl;

import com.guocanjie.dao.mapper.ArticleBodyMapper;
import com.guocanjie.dao.pojo.ArticleBody;
import com.guocanjie.service.ArticleBodyService;
import com.guocanjie.utils.vo.ArticleBodyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Override
    public ArticleBodyVo getArticleBodyById(Long id) {
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        return copy(articleBody);
    }

    public ArticleBodyVo copy(ArticleBody articleBody){
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        BeanUtils.copyProperties(articleBody,articleBodyVo);
        return articleBodyVo;
    }
}
