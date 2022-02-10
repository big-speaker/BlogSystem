package com.guocanjie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guocanjie.dao.mapper.CategoryMapper;
import com.guocanjie.dao.pojo.Category;
import com.guocanjie.service.CategoryService;
import com.guocanjie.utils.Request;
import com.guocanjie.utils.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo getCategoryById(Long categoryId) {
//        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(Category::getId, categoryId);
        Category category = categoryMapper.selectById(categoryId);
        return copy(category);
    }

    @Override
    public Request findAll() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        List<CategoryVo> categoryVoList = copyList(categories);
        return new Request(categoryVoList);
    }

    @Override
    public Request findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        return new Request(categories);
    }

    @Override
    public Request findCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        return new Request(copy(category));
    }

    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

}
