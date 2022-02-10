package com.guocanjie.utils;

import lombok.Data;

@Data
public class PageParams {

    private int page;
    private int pageSize;
    private Long categoryId;
    private Long tagId;
}
