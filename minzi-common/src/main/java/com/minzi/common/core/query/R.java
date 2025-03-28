package com.minzi.common.core.query;


import java.util.HashMap;
import java.util.Map;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        r.put("code", 200);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        R r = new R();
        r.put("code",200);
        return r;
    }

    public R setData(Object data) {
        this.put("code", 200);
        this.put("data", data);
        return this;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static void dataParamsAssert(boolean condition,String message){
        if (condition){
            throw new RuntimeException(message);
        }
    }
}
