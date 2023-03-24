package com.example.emos.wx.common.util;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String,Object> {
    public R() {
        this.put((String)"code", 200);
        this.put((String)"msg", "success");
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static R ok() {
        return new R();
    }

    public static R ok(String msg) {
        R r = new R();
        r.put((String)"msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put((String)"code", code);
        r.put((String)"msg", msg);
        return r;
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }
}
