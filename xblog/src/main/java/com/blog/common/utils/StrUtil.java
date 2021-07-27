package com.blog.common.utils;

import com.blog.common.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.smartcardio.CardTerminal;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转换工具
 *
 * @author Avalon
 * @date 2019/1/31
 */
@Slf4j
public class StrUtil
{

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str)
    {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str)
    {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 从文章中获取图片 最多获取3张图片
     *
     * @param content
     * @return
     */
    public static String findImageByContent(String content)
    {
        if (StringUtils.isBlank(content))
        {

            return "";
        }
        Pattern tagPattern = Pattern.compile("(http|https):.*?-.*?-.*?-.*?-.*?\"-\".*?\\)");
        Pattern imagePattern = Pattern.compile("(http|https):.*?-.*?-.*?-.*?-[A-Za-z0-9]{12}");

        Matcher tagMatcher = tagPattern.matcher(content);
        JSONArray images = new JSONArray();
        while (tagMatcher.find())
        {
            String imageStr = tagMatcher.group(0);
            Matcher imageMatcher = imagePattern.matcher(imageStr);
            while (imageMatcher.find())
            {
                String path = imageMatcher.group(0);
                String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                File picture = new File(WebUtils.getFileABSPath(fileName));
                if (picture == null)
                {
                    continue;
                }
                JSONObject imageObj = new JSONObject();
                BufferedImage sourceImg = null;
                try
                {
                    sourceImg = ImageIO.read(new FileInputStream(picture));
                }
                catch (IOException e)
                {
                    log.error("博客文章整理图片时异常", e);
                }
                imageObj.put("src", path);
                imageObj.put("width", sourceImg.getWidth());
                imageObj.put("height", sourceImg.getHeight());
                //是否是大图展示
                imageObj.put("isBig", sourceImg.getHeight() > 100 && sourceImg.getWidth() > 300);
                images.add(imageObj);
            }
            if (images.size() == 3)
            {
                break;
            }
        }
        return images.toString();
    }

    /**
     * 从文章中获取简介
     *
     * @param content
     * @return
     */
    public static String findIntro(String content)
    {

        String[] split = content.split("<!-- title -->");
        if (split != null && split.length >= 2)
        {
            return split[0];
        }
        return content.substring(0, content.length() > 100 ? 100 : content.length());
    }
}
