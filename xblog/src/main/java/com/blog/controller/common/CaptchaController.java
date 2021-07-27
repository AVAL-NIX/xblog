package com.blog.controller.common;

import com.blog.common.utils.CacheUtils;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;

/**
 * 验证码控制类
 *
 * @author zx
 * @date 2019/1/28
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends BaseController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private CacheUtils cacheUtils;

    /**
     * 生成验证码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void captcha() throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        // 验证码存入缓存 TODO
//        cacheUtils.set(CacheUtil.FIVE_MINUTES, CacheKey.CACHE_IMG_CODE + session.getId(), capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
