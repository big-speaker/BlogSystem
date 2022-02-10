package com.guocanjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.TagMapper;
import com.guocanjie.dao.pojo.Tag;
import com.guocanjie.service.TagService;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> getTag(Long articleId) {
        List<Tag> tagList= tagMapper.getTagByArcitleId(articleId);
        return copyList(tagList);
    }

    @Override
    public Request getHotTag(int limit) {
        List<Long> tagIdList = tagMapper.getHotTagByLimit(limit);
        if(CollectionUtils.isEmpty(tagIdList)){
            return new Request(200,"没有热门",null);
        }
        List<TagVo> tagVoList = new ArrayList<>();
        for (Long aLong : tagIdList) {
            Tag tag = tagMapper.selectById(aLong);
            tagVoList.add(copy(tag));
        }
        return new Request(tagVoList);
    }

    @Override
    public Request getTags() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVoList = copyList(tagList);
        return new Request(tagVoList);
    }

    @Override
    public Request getTagsDetail() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        return new Request(tagList);
    }

    @Override
    public Request getTagsById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return new Request(copy(tag));
    }

    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }


}
