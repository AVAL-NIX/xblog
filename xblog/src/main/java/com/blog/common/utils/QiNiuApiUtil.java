package com.blog.common.utils;

import com.blog.common.constants.QiniuKey;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @author potatoZ
 * @date 2021/7/29  14:37
 * 7牛云api
 */
@Slf4j
public class QiNiuApiUtil {

    /**
     * 上传文件到 7牛云
     */
    public static Response uploadFileToQiNiu(byte[] bytes, String fileName){
        //获取配置
        Configuration cfg = settingConfiguration();
        UploadManager uploadManager = new UploadManager(cfg);
        Response rs = null;
        try {
            //getQiNiuUpToken 获取 鉴权token
             rs = uploadManager.put(bytes,fileName, getQiNiuUpToken());
        } catch (QiniuException e) {
           // 输出错误
            log.error("上传7牛云失败", e);
        }
        return rs;
    }

    /**
     * 获取该空间下所有文件
     * @param bucketName 空间名称
     * @param prefix 文件名前缀
     * @param limit 每次迭代的长度限制，最大1000，推荐值 1000
     * @param delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @return
     */
    public static List<FileInfo[]> getBucketNameAllFile(String bucketName, String prefix, int limit, String delimiter){
        BucketManager bucketManager = new BucketManager(createAuth(), settingConfiguration());
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);
        List<FileInfo[]> list = new ArrayList<>();
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            list.add(fileListIterator.next());
        }
        return list;
    }



    /**
     *  配置 区域
     *     华东	Region.region0(), Region.huadong()
     *     华北	Region.region1(), Region.huabei()
     *     华南	Region.region2(), Region.huanan()
     *     北美	Region.regionNa0(), Region.beimei()
     *     东南亚	Region.regionAs0(), Region.xinjiapo()
     * @return
     */
    public static Configuration settingConfiguration(){
        Configuration cfg = new Configuration(Region.region2());
        cfg.useHttpsDomains = false;
        return cfg;
    }

    /**
     * 获取7牛云 上传授权token
     * @return 返回授权码
     */
    public static String getQiNiuUpToken(){
        Auth auth = createAuth();
        return auth.uploadToken(QiniuKey.BUCKET_NAME);
    }

    /**
     * 创建 Auth 对象
     * @return
     */
    public static Auth createAuth(){
        return Auth.create(QiniuKey.ACCESS_KEY, QiniuKey.SECRET_KEY);
    }













}
