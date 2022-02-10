package com.guocanjie.controller;

import com.guocanjie.service.CategoryService;
import com.guocanjie.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Request getCategory(){
        return categoryService.findAll();
    }

    @GetMapping("/detail")
    public Request getCategoryDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("/detail/{id}")
    public Request getCategoryById(@PathVariable Long id){
        return categoryService.findCategoryById(id);
    }
}
