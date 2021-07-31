package com.blog.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author potatoZ
 * @date 2021/7/28  16:18
 */
@Component
public class QiniuKey {
    /** 上传空间名 **/
    public static String BUCKET_NAME;
    @Value("${qiNiu.bucketName}")
    public  void setbucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }

    /** 访问 key **/
    public static String ACCESS_KEY;
    @Value("${qiNiu.accessKey}")
    public  void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }


    /** 秘钥 key **/
    public static String SECRET_KEY;
    @Value("${qiNiu.secretKey}")
    public  void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    /** 7牛云资源公有访问地址 **/

    public static String ACCESS_URL;
    @Value("${qiNiu.accessUrl}")
    public  void setAccessUrl(String accessUrl) {
        ACCESS_URL = accessUrl;
    }
}
