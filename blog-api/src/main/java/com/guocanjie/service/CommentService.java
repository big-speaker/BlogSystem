package com.guocanjie.service;

import com.guocanjie.dao.mapper.CommentMapper;
import com.guocanjie.utils.CommentParams;
import com.guocanjie.utils.Request;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;

public interface CommentService {

    /**
     * 根据文章id查询评论
     * @return
     */
    public Request getComment(Long id);

    /**
     * 发表评论
     * @param commentParams
     * @return
     */
    public Request insertComment(CommentParams commentParams);
}
