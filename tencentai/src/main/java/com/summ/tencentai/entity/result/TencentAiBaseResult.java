package com.summ.tencentai.entity.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summ.tool.JsonUtils;

import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

public class TencentAiBaseResult<T> implements Serializable {
    /**
     * ret : 0
     * msg : ok
     * data : {}
     */

    private int ret;
    private String msg;
    private String data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data == null ? "" : data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TencentAiBaseResult{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public T getDataObject(Class<T> clz) {
        return JsonUtils.fromJson(getData(), clz);
    }

    public List<T> getDataList(Class<T> clz) {
        org.json.JSONObject jsonObject = null;
        try {
            jsonObject = new org.json.JSONObject(getData());
            String jsonStr = jsonObject.getString("item_list");
            return JsonUtils.fromJsonList(jsonStr, clz);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
