package com.summ.tencentai.entity.param;

import com.summ.tool.EmptyUtils;
import com.summ.tool.LogUtils;
import com.summ.tool.MD5Utils;

import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

public abstract class BaseTencentAiParam {

    public final Map<String, Object> bodyParam = new HashMap<>();

    /**
     * 参数签名
     *
     * @return sign info
     */
    public abstract Map<String, Object> getRequestMap();

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    public int getSecondTimestamp() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return Integer.valueOf(timestamp);
    }

    public String getRandomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 参数签名
     *
     * @param param 参数集合
     * @param appkey app key
     * @return sign string
     */
    public String getReqSign(Map<String, Object> param, String appkey) {
        if (EmptyUtils.isEmpty(param)) {
            return "";
        }
        param = sortMapByKey(param);
        Set<Map.Entry<String, Object>> set = param.entrySet();
        StringBuilder bodyBuilder = new StringBuilder();
        int pos = 0;
        for (Map.Entry<String, Object> entry : set) {
            String value = entry.getValue().toString();
            if (EmptyUtils.isEmpty(value)) {
                continue;
            }
            String temp = String.format("%s=%s", entry.getKey(), URLEncoder.encode(value));
            if (pos > 0) {
                bodyBuilder.append("&");
            }
            bodyBuilder.append(temp);
            pos++;
        }

        String app_Key = "&app_key=" + appkey;

        String paramInfo = bodyBuilder.append(app_Key).toString().trim();
        String signStr = MD5Utils.MD5Encode(paramInfo, "utf-8").toUpperCase();

        LogUtils.i("Param: " + signStr);
        return signStr;
    }


    /**
     * 使用 Map按key进行排序
     *
     * @param map 待排序的Map
     * @return 已排序Map
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(new MapComparatorAsc());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 升序比较器类
     */
    public static class MapComparatorAsc implements Comparator<String> {
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 降序比较器类
     */
    public static class MapComparatorDesc implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str2.compareTo(str1);
        }
    }


}
