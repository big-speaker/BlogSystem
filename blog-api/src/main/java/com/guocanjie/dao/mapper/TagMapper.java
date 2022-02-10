package com.guocanjie.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guocanjie.dao.pojo.Tag;
import com.guocanjie.utils.vo.TagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

//    通过多表查询,查询作品标签
    @Select("select ms_tag.id,ms_tag.avatar,ms_tag.tag_name as tagName from ms_tag,ms_article_tag where ms_article_tag.tag_id = ms_tag.id and ms_article_tag.article_id = #{articleId}")
    public List<Tag> getTagByArcitleId(Long articleId);

//    查询热门标签
    @Select("select tag_id from ms_article_tag group by tag_id order by count(*) limit #{limit}")
    public List<Long> getHotTagByLimit(int limit);

}
