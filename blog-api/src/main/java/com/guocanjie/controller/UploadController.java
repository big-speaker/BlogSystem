package com.guocanjie.controller;

import com.guocanjie.utils.QiNiuUtils;
import com.guocanjie.utils.Request;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    String url = "http://r6t371fuw.hn-bkt.clouddn.com/";

    @Autowired
    private QiNiuUtils qiNiuUtils;

    @PostMapping
    public Request upload(@RequestParam("image") MultipartFile file){
//        获取文件的原始名 aa.png
        String originalFilename = file.getOriginalFilename();
//        用UUID将文件重新取名
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename,".");
//        将文件上传到云服务器
        Boolean upload = qiNiuUtils.upload(file,fileName);
        if(upload){
            return new Request(url+fileName);
        }else{
            return new Request("图片上传失败");
        }
    }
}
