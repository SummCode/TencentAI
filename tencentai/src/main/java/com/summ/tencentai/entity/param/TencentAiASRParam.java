package com.summ.tencentai.entity.param;

import java.util.Map;

public class TencentAiASRParam extends BaseTencentAiParam {


    /**
     *
     appId	是	int	正整数	1000001	应用标识（AppId）
     timeStamp	是	int	正整数	1493468759	请求时间戳（秒级）
     nonceStr	是	string	非空且长度上限32字节	fa577ce340859f9fe	随机字符串
     sign	是	string	非空且长度固定32字节	B250148B284956EC5218D4B0503E7F8A	签名信息，详见接口鉴权
     format	是	int	正整数	2	语音压缩格式编码，定义见下文描述
     speech	是	string	语音数据的Base64编码，非空且长度上限8MB		待识别语音（时长上限30s）
     rate	否	int	正整数	16000	语音采样率编码，定义见下文描述，（不传）默认即16KHz
     */

    /**
     * 应用标识（AppId）
     */
    private int appId;

    /**
     * 请求时间戳（秒级）
     */
    private int timeStamp;

    /**
     * 非空且长度上限32字节	fa577ce340859f9fe	随机字符串
     */
    private String sign = "";

    /**
     * 非空且长度上限32字节	fa577ce340859f9fe	随机字符串
     */
    private String nonceStr;

    /**
     * 语音压缩格式编码
     */
    private int format;

    /**
     * 语音采样率编码，定义见下文描述，（不传）默认即16KHz
     */
    private int rate;

    /**
     * 语音数据的Base64编码，非空且长度上限8MB		待识别语音（时长上限30s）
     */
    private String speech;


    public TencentAiASRParam(int appId, int format, String speech) {
        this.appId = appId;
        this.timeStamp = getSecondTimestamp();
        this.nonceStr = getRandomStr();
        this.format = format;
        this.speech = speech;
        this.rate = 16000;
    }

    public TencentAiASRParam(int appId, int format, String speech, int rate) {
        this.appId = appId;
        this.timeStamp = getSecondTimestamp();
        this.nonceStr = getRandomStr();
        this.format = format;
        this.speech = speech;
        this.rate = rate;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonceStr() {
        return nonceStr == null ? "" : nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getSpeech() {
        return speech == null ? "" : speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    @Override
    public String toString() {
        return "TencentAiASRParam{" +
                "appId=" + appId +
                ", timeStamp=" + timeStamp +
                ", sign='" + sign + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", format=" + format +
                ", rate=" + rate +
                ", speech='" + speech + '\'' +
                '}';
    }

    @Override
    public Map<String, Object> getRequestMap() {
        bodyParam.put("app_id", this.appId);
        bodyParam.put("time_stamp", this.timeStamp);
        bodyParam.put("nonce_str", this.nonceStr);
        bodyParam.put("format", this.format);
        bodyParam.put("rate", this.rate);
        bodyParam.put("sign", this.sign);
        bodyParam.put("speech", this.speech);
        return bodyParam;
    }
}
