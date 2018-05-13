package com.summ.tencentai.api;

public class TencentAiApi {

    /**
     * 语音识别-echo版
     * 1. 接口描述
     * 语音识别-echo版接口提供在线识别语音的能力，对整段音频进行识别，识别完成后，将返回语音的文字内容。
     * <p>
     * 注意事项：
     * 1、待识别语音（时长上限30s）
     * 2、语音数据的Base64编码，非空且长度上限8MB
     * 3、只支持中文普通话语音识别，后续开放更多语种的识别能力
     */
    public final static String TENCENT_AI_AAI_ASR = "https://api.ai.qq.com/fcgi-bin/aai/aai_asr";

    /**
     * 身份证OCR识别接口
     * 1. 接口描述
     * 根据用户上传的包含身份证正反面照片，识别并且获取证件姓名、性别、民族、出生日期、地址、身份证号、证件有效期、发证机关等详细的身份证信息，
     * 并且可以返回精确剪裁对齐后的身份证正反面图片。
     * <p>
     */
    public final static String TENCENT_AI_OCR_IDCARDOCR = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_idcardocr";

    /**
     * 行驶证驾驶证OCR识别接口
     * 1. 接口描述
     * 根据用户上传的图像，返回识别出行驶证、驾驶证各字段信息。
     * 行驶证支持字段：车牌号码、车辆类型、所有人、住址、使用性质、品牌型号、识别代码、发动机号、注册日期、发证日期；
     * 驾驶证支持字段：证号、姓名、性别、国籍、住址、出生日期、领证日期、准驾车型、起始日期、有效日期；
     * <p>
     */
    public final static String TENCENT_AI_OCR_DRIVER_LICENSE = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_driverlicenseocr";

    /**
     * 营业执照OCR识别接口
     * 1. 接口描述
     * 营业执照OCR 识别，根据用户上传的营业执照图像，返回识别出的注册号、公司名称、地址字段信息。
     * <p>
     */
    public final static String TENCENT_AI_OCR_BIZ_LICENSE = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_bizlicenseocr";


    public final static String TENCENT_AI_OCR_CREDIT_CARD = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_creditcardocr";


}
