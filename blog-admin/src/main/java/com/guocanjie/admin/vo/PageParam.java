package com.guocanjie.admin.vo;

import lombok.Data;

@Data
public class PageParam {

    private Integer currentPage;
    private Integer pageSize;
    private String queryString;
    private Integer total;
}
