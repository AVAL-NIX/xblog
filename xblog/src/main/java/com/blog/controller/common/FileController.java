package com.blog.controller.common;

import com.blog.common.constants.AppConstants;
import com.blog.common.utils.WebUtils;
import com.blog.model.bean.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
        String filePath = WebUtils.getProjectUrl() + AppConstants.UPLOAD_PATH;
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File dest = new File(filePath + fileName);
        try {
            //TODO 这里要上传到 7 牛云
            multipartFile.transferTo(dest);
            log.info("上传成功! " + fileName);

            return ResultData.ok(WebUtils.getBaseUrl() + AppConstants.UPLOAD_PATH + fileName);
        } catch (IOException e) {
            log.error("上传异常!", e);
        }
        return ResultData.error("上传失败");
    }

}
