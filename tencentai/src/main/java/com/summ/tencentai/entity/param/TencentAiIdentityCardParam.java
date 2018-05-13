package com.summ.tencentai.entity.param;

import java.util.Map;

public class TencentAiIdentityCardParam extends BaseTencentAiParam {

    private int appId;
    private int timeStamp;
    private String nonceStr;
    private String sign;
    private String image;
    private int cardType;

    /**
     * @param appId    应用标识（AppId） 1000001
     * @param image    待识别图片 原始图片的base64编码数据（原图大小上限1MB，支持JPG、PNG、BMP格式）
     * @param cardType 身份证图片类型，0-正面，1-反面
     */
    public TencentAiIdentityCardParam(int appId, String image, int cardType) {
        this.appId = appId;
        this.timeStamp = getSecondTimestamp();
        this.nonceStr = getRandomStr();
        this.sign = "";
        this.image = image;
        this.cardType = cardType;
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

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "TencentAiIdentityCardParam{" +
                "appId=" + appId +
                ", timeStamp=" + timeStamp +
                ", nonceStr='" + nonceStr + '\'' +
                ", sign='" + sign + '\'' +
                ", image='" + image + '\'' +
                ", cardType=" + cardType +
                '}';
    }

    @Override
    public Map<String, Object> getRequestMap() {
        bodyParam.put("app_id", appId);
        bodyParam.put("time_stamp", timeStamp);
        bodyParam.put("nonce_str", nonceStr);
        bodyParam.put("sign", sign);
        bodyParam.put("image", image);
        bodyParam.put("card_type", cardType);
        return bodyParam;
    }
}
