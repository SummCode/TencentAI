package com.summ.tool;

import android.database.Cursor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Project Name：Common
 * Package Name：com.summ.base.tools
 * @author:summ.Chen
 * Create：2017/8/28
 * Email： summ_summ@163.com
 * Version：1.0.0
 * <p>
 * Describe：对象空的判断
 */

public class EmptyUtils {

    /**
     * 判断字符串是否为空
     *
     * @param sequence 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(CharSequence sequence) {
        return isEmpty(sequence.toString());
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(String str) {
        if (str != null && !str.isEmpty() && !str.equals("null")) {
            return false;
        }
        return true;
    }

    /**
     * 判断Json字符串是否为空
     *
     * @param jsonStr 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isJsonEmpty(String jsonStr) {
        if (jsonStr != null && !jsonStr.isEmpty() && !jsonStr.equals("{}") && !jsonStr.equals("null")) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串数组是否为空
     *
     * @param str 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(String[] str) {
        if (str != null && str.length > 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串数组是否为空
     *
     * @param files 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(File[] files) {
        if (files != null && files.length > 0){
            return false;
        }
        return true;
    }

    /**
     * 判断List集合是否为空
     *
     * @param list 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(List list) {
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 判断Set集合是否为空
     *
     * @param set 要检查的对象
     * @return true: null or 对象大小为0
     */
    public static boolean isEmpty(Set set) {
        if (set != null && !set.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * 判断Map集合是否为空
     *
     * @param map 要检查的对象
     * @return true: null or 对大小为0
     */
    public static boolean isEmpty(Map map) {
        if (map != null && !map.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0){
            return false;
        }
        return true;
    }

}
