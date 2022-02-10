package com.guocanjie.controller;

import com.guocanjie.service.TagService;
import com.guocanjie.utils.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Request getHotTag(){
        int limit = 2;
        return tagService.getHotTag(limit);
    }

    @GetMapping
    public Request getTags(){
        return tagService.getTags();
    }

    @GetMapping("/detail")
    public Request getTagsDetail(){
        return tagService.getTagsDetail();
    }

    @GetMapping("/detail/{id}")
    public Request getTagsById(@PathVariable Long id){
        return tagService.getTagsById(id);
    }
}
