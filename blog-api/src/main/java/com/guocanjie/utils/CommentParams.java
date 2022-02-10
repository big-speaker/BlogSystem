package com.guocanjie.utils;

import lombok.Data;

@Data
public class CommentParams {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
