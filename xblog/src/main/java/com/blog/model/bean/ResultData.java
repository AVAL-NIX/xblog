package com.blog.model.bean;


import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * 输出类 {@link ResultData}
 *
 * @author zx
 * @date 2019/2/11
 */
@Data
public class ResultData<T> {

    /**
     * 处理码
     */
    private int code = -1;

    /**
     * 消息
     */
    private String msg = "success";

    /**
     * 处理数量
     */
    private long count = 0;

    /**
     * 泛型数据类
     */
    private T data = null;

    /**
     * 普通失败
     *
     * @return
     */
    public static ResultData error() {
        return error(-500, "未知异常，请联系管理员");
    }

    /**
     * 普通失败
     *
     * @param msg
     * @return
     */
    public static ResultData error(String msg) {
        return error(-1, msg);
    }

    /**
     * 普通失败
     *
     * @param code
     * @param msg
     * @return
     */
    public static ResultData error(int code, String msg) {
        ResultData r = new ResultData();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 泛型失败
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultData<T> error(String msg, T data) {
        ResultData r = new ResultData();
        r.setCode(-1);
        r.setMsg(msg);
        return r;
    }

    /**
     * 泛型成功
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ResultData<T> ok(String msg) {
        ResultData r = new ResultData();
        r.setCode(1);
        r.setMsg(msg);
        return r;
    }

    /**
     * 泛型成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultData<T> data(T data) {
        ResultData r = new ResultData();
        r.setCode(1);
        r.setData(data);
        return r;
    }


    /**
     * 泛型处理成功
     *
     * @param msg
     * @param data
     * @return
     */
    public static <T> ResultData<T> ok(String msg, T data) {
        ResultData r = new ResultData();
        r.setCode(1);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 默认成功
     *
     * @return
     */
    public static ResultData ok() {
        return new ResultData(1, "处理成功!");
    }


    /**
     * 构造函数
     *
     * @param code
     * @param msg
     */
    public ResultData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResultData page(Page<T> all) {
        ResultData r = ResultData.ok();
        r.setMsg("查询成功");
        r.setData(all);
        return r;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isOk() {
        if (this.code > 0) {
            return true;
        }
        return false;
    }

    public ResultData() {
    }

}
