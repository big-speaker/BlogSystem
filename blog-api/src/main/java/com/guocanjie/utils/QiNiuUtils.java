package com.guocanjie.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@Component
public class QiNiuUtils {

    public Boolean upload(MultipartFile file, String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "H9ELtOdag71mObvr4bxLozWduCoVJsPnT4ktbwDk";
        String secretKey = "uQdAA2_9Tf_o2lCwsPYUFdWFdBGMg8-sCGZ4kwPi";
        String bucket = "pictureplace";
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
                //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
          return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
}
