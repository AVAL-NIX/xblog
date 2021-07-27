package com.blog.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * JSON格式类
 *
 * @author zx
 * @date 2019/1/28
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将java对象转换成json字符串
     *
     * @param bean
     * @return
     */
    public static String bean2Json(Object bean) {
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            log.error(" 将java对象转换成json字符串    error ! ",e);
        } finally {
            return jsonString;
        }

    }

    /**
     * json 字符串转 java 对象
     *
     * @param json
     * @param objClass
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String json , Class<T> objClass){
        T obj = null;
        try {
            obj =  mapper.readValue(json, objClass);
        } catch (IOException e) {
            log.error(" json 字符串转 对象 error ! ",e);
        } finally {
            return obj;
        }
    }


    private JsonUtil() {

    }
}
