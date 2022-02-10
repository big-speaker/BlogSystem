package com.guocanjie.service;

import com.guocanjie.dao.pojo.Tag;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.TagVo;

import java.util.List;

public interface TagService {

//    将查询到的Tag列表包装成TagVo列表
    public List<TagVo> getTag(Long articleId);

//    查询最热门标签数据返回
    public Request getHotTag(int limit);

//    查询所有文章标签
    public Request getTags();

//    查询所有文章标签详情
    public Request getTagsDetail();

//    查询单个文章标签详情
    public Request getTagsById(Long id);
}
