package com.guocanjie.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guocanjie.dao.pojo.Archives;
import com.guocanjie.dao.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT FROM_UNIXTIME(create_date/1000, '%Y') as year, FROM_UNIXTIME(create_date/1000, '%m') as month, count(*) as count FROM ms_article GROUP BY year,month limit #{limit}")
    public List<Archives> getArchives(int limit);
}
