package com.summ.tencentai.entity.param;

import java.util.Map;

public class TencentAiBusinessLicenseParam extends BaseTencentAiParam {

    private int appId;
    private int timeStamp;
    private String nonceStr;
    private String sign;
    private String image;

    /**
     * @param appId 应用标识（AppId） 100001
     * @param image 待识别图片 原始图片的base64编码数据（原图大小上限1MB，支持JPG、PNG、BMP格式)
     */
    public TencentAiBusinessLicenseParam(int appId, String image) {
        this.appId = appId;
        this.timeStamp = getSecondTimestamp();
        this.nonceStr = getRandomStr();
        this.sign = "";
        this.image = image;
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

    public String getNonceStr() {
        return nonceStr == null ? "" : nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "TencentAiVehicleLicenseParam{" +
                "appId=" + appId +
                ", timeStamp=" + timeStamp +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                ", image='" + image +
                '}';
    }

    @Override
    public Map<String, Object> getRequestMap() {
        bodyParam.put("app_id", appId);
        bodyParam.put("time_stamp", timeStamp);
        bodyParam.put("nonce_str", nonceStr);
        bodyParam.put("sign", sign);
        bodyParam.put("image", image);
        return bodyParam;
    }


}
