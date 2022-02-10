package com.guocanjie.utils;

import com.guocanjie.utils.vo.CategoryVo;
import com.guocanjie.utils.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParams {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
