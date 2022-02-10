package com.guocanjie;

import com.guocanjie.dao.mapper.TagMapper;
import com.guocanjie.dao.pojo.Tag;
import com.guocanjie.service.ArticleService;
import com.guocanjie.service.SysUserService;
import com.guocanjie.service.TagService;
import com.guocanjie.utils.PageParams;
import com.guocanjie.utils.vo.TagVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.dc.pr.PRError;

import java.util.List;

@SpringBootTest(classes = BlogApplication.class)
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleService articleService;

    /**
     * 测试tagService业务
     */
    @Test
    public void Test01(){

        Long id = 1L;
        List<TagVo> tagVoList = tagService.getTag(id);
        System.out.println("--------------结果-----------"+tagVoList);
    }

    /**
     * 测试tagMapper业务中的多表查询业务
     */
    @Test
    public void Test02(){
        List<Tag> tagList = tagMapper.getTagByArcitleId(1L);
        System.out.println("结果输出"+tagList);
    }

    /**
     * 测试sysUserService业务
     */
    @Test
    public void  Test03(){
        System.out.println(sysUserService.getSysUser(6L));
    }

    /**
     * 测试ArticleService业务
     */
    @Test
    public void Test04(){
        PageParams pageParams = new PageParams();
        pageParams.setPage(1);
        pageParams.setPageSize(1);
        System.out.println(articleService.getPage(pageParams));
    }

    @Test
    public void Test05(){
        System.out.println(tagService.getHotTag(2));
    }


    @Test
    public void Test06(){
        String password = "liuwenfeng";
        String newPassword = DigestUtils.md5Hex(password+"guocanjie##");
        System.out.println(newPassword);
    }
}
