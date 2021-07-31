package com.blog.controller.common;

import com.blog.common.constants.QiniuKey;
import com.blog.common.utils.QiNiuApiUtil;
import com.blog.model.annotation.PassToken;
import com.blog.model.bean.ResultData;
import com.qiniu.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传控制类
 *
 * @author zx
 * @date 2019/3/30
 */
@RequestMapping("/file")
@RestController
@Slf4j
public class FileController extends BaseController {

    @RequestMapping("/upload")
    @PassToken
    public ResultData upload(@RequestParam("multipartFile") MultipartFile multipartFile) {
        // 权限验证
        ResultData r = getAdminByAccessToken();
        if (!r.isOk()) {

            return r;
        }
        if (multipartFile.isEmpty()) {

            return ResultData.error("上传失败");
        }
        String fileName = UUID.randomUUID().toString();
        try {
            Response rs = QiNiuApiUtil.uploadFileToQiNiu(multipartFile.getBytes(), fileName);
            if (rs == null) {
                ResultData.error("上传失败");
            }

            if (rs.statusCode == 200) {
                return ResultData.ok(QiniuKey.ACCESS_URL + fileName);
            }

        } catch (IOException e) {
            log.error("上传异常!", e);
        }
        return ResultData.error("上传失败");
    }

}
