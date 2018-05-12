package com.summ.tool;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Describe：
 * <p>
 * Json数据解析的工具类
 * </p>
 * @author:summ.Chen.
 * Create：2016/10/15.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class JsonUtils {


    /**
     * 将对象解析成jsonString
     *
     * @param obj 要解析的对象
     * @return jsonStr
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 将jsonStr解析成对象
     *
     * @param json json字符串
     * @param clz  要转换的class
     * @return T
     */
    public static <T> T fromJson(String json, Class<T> clz) {

        if (EmptyUtils.isJsonEmpty(json)) {
            return null;
        }

        if (clz == null) {
            return null;
        }

        try {
            return parseObject(json, clz);
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return null;
    }

    /**
     * 将jsonStr解析成List集合
     *
     * @param json JsonStr
     * @param clz  要转换的class
     * @param <T>  T
     * @return ArraryList
     */
    public static <T> List<T> fromJsonList(String json, Class<T> clz) {

        if (EmptyUtils.isJsonEmpty(json)) {
            return null;
        }

        if (clz == null) {
            return null;
        }

        try {
            return JSON.parseArray(json, clz);
        } catch (Exception e) {
            LogUtils.e(e);
        }

        return null;
    }


    /**
     * 将jsonStr解析成List集合
     *
     * @param json JsonStr
     * @param type 要转换的class
     * @param <T>  T
     * @return ArraryList
     */
    public static <T> T fromJsonObject(String json, TypeReference<T> type) {

        if (EmptyUtils.isJsonEmpty(json)) {
            return null;
        }

        if (type == null) {
            return null;
        }

        try {
            return JSON.parseObject(json, type);
        } catch (Exception e) {
            LogUtils.e(e);
        }

        return null;
    }

    /**
     * 检查字符串是否json格式
     * @param json  json字符串
     * @return true:json string ;false:bad json
     */
    public static boolean isJsonStr(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            LogUtils.e("bad json: " + json);
            return false;
        }
    }


}
