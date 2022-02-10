package com.guocanjie.service;

import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    /**
     * 根据id查询文章类型
     * @param categoryId
     * @return
     */
    public CategoryVo getCategoryById(Long categoryId);

    /**
     * 查询所有文章类型
     * @return
     */
    public Request findAll();

    /**
     * 查询所有文章类型详情
     * @return
     */
    public Request findAllDetail();

    /**
     * 查询单个category详情
     * @return
     */
    public Request findCategoryById(Long id);
}
