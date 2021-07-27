package com.blog.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.common.constants.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileNotFoundException;

/**
 * WEB 工具类
 *
 * @author zx
 * @date 2019/2/10
 */
public final class WebUtils {

    private static final String UNKOWN = "unknown";



    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @return 客户端真实IP
     */
    public static String getIp() {
        return getIp(getRequest());
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request HttpServletRequest
     * @return 客户端真实IP
     */
    public static String getIp(HttpServletRequest request) {
        // Nginx 代理获取需要通过x-real-ip 或者 x-forwarded-for
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip) && ip.contains(",")) {
            String[] ips = ip.split(",");
            ip = ips[ips.length - 1];
        }
        return ip;
    }

    /**
     * 获取Referer
     *
     * @return Referer
     */
    public static String getReferer() {
        return getReferer(getRequest());
    }

    /**
     * 获取Referer
     *
     * @return Referer
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        return servletRequestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        return servletRequestAttributes.getResponse();
    }

    public static String getContextPath() {
        return getRequest().getContextPath();
    }

    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    /**
     * 获取站点Baseurl
     *
     * @return BaseUrl
     */
    public static String getBaseUrl() {
        return getBaseUrl(getRequest());
    }

    /**
     * 获取站点Baseurl
     * 
     * @param request HttpServletRequest
     * @return BaseUrl
     */
    public static String getBaseUrl(HttpServletRequest request) {
        int port = request.getServerPort();
        if (request.getServerPort() == 80 || request.getServerPort() == 443) {
            return request.getScheme() + "://" + request.getServerName();
        }
        return request.getScheme() + "://" + request.getServerName() + ":" + port;
    }

    /**
     * 是否ajax请求
     * 
     * @return {@code true：是，false：否}
     */
    public static boolean isAjax() {
        return isAjax(getRequest());
    }

    /**
     * 是否ajax请求
     * 
     * @param request HttpServletRequest
     * @return {@code true：是，false：否}
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取 Origin
     * 
     * @return
     */
    public static String getOrigin() {
        HttpServletRequest request = getRequest();
        return request.getHeader("Origin");
    }

    /**
     * 获取项目跟路径: 此路径有问题.
     * home\web\blog\WEB-INF\classes\static\
     * @return
     */
    public static String getUrl() {
        String path = null;
        try {
            String serverpath =
                ResourceUtils.getURL("classpath:static").getPath().replace("%20", " ").replace('/', '\\');
            path = serverpath.substring(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取项目跟路径: 不带static文件夹
     * /home/web/blog/WEB-INF/classes/
     * @return
     */
    public static String getProjectUrl() {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }

    /**
     * 获取系统文件URL
     * /home/tomcat9
     * @return
     */
    public static String getSystemUrl() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取文件路径
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileABSPath(String fileName){
        return WebUtils.getProjectUrl() + AppConstants.UPLOAD_PATH +fileName;
    }



}
