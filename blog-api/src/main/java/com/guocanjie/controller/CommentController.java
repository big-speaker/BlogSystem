package com.guocanjie.controller;

import com.guocanjie.service.CommentService;
import com.guocanjie.utils.CommentParams;
import com.guocanjie.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 请求路径 comments/article/{id}
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    public Request comment(@PathVariable Long id){
        return commentService.getComment(id);
    }

    /**
     * 发表评论
     * @param commentParams
     * @return
     */
    @PostMapping("/create/change")
    public Request createComment(@RequestBody CommentParams commentParams){
        return commentService.insertComment(commentParams);
    }
}
