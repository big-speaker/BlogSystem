package com.guocanjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.CommentMapper;
import com.guocanjie.dao.pojo.Comment;
import com.guocanjie.dao.pojo.SysUser;
import com.guocanjie.service.CommentService;
import com.guocanjie.service.SysUserService;
import com.guocanjie.service.ThreadService;
import com.guocanjie.utils.CommentParams;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.UserThreadLocal;
import com.guocanjie.utils.vo.CategoryVo;
import com.guocanjie.utils.vo.CommentVo;
import com.guocanjie.utils.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Request getComment(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId, id);
        lambdaQueryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(lambdaQueryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return new Request(commentVos);
    }

    @Override
    public Request insertComment(CommentParams commentParams) {
        SysUser sysUser = UserThreadLocal.get();
        Long parent = commentParams.getParent();
        Long toUserId = commentParams.getToUserId();
        Comment comment = new Comment();
        comment.setContent(commentParams.getContent());
        comment.setArticleId(commentParams.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setParentId(parent == null ? 0 : parent);
        comment.setToUid(toUserId == null ? 0 : toUserId);
        comment.setCreateDate(System.currentTimeMillis());
        if(parent == null){
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        return new Request(commentMapper.insert(comment));
    }


    /**\
     * comment对象转换成commentVo对象
     * @param comments
     * @return
     */
    public List<CommentVo> copyList(List<Comment> comments){
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    public CommentVo copy(Comment comment){
//        查找评论内容
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);

//        查找作者信息
        UserVo userVo = sysUserService.getUserVo(comment.getAuthorId());
        commentVo.setAuthor(userVo);

//        查找子评论
        if(comment.getLevel() == 1){
            Long commentId = comment.getId();
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getParentId,commentId);
            lambdaQueryWrapper.eq(Comment::getLevel,2);
            List<Comment> children = commentMapper.selectList(lambdaQueryWrapper);
            commentVo.setChildrens(copyList(children));
        }

//        查找给谁评论
        if(comment.getLevel() > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.getUserVo(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }
}
