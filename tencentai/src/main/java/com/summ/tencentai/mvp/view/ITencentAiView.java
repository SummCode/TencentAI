package com.summ.tencentai.mvp.view;

import com.summ.network.response.body.ResponseResult;
import com.summ.tencentai.entity.result.TencentAiASRResult;
import com.summ.tencentai.entity.result.TencentAiBusinessLicenseResult;
import com.summ.tencentai.entity.result.TencentAiCreditCardResult;
import com.summ.tencentai.entity.result.TencentAiIdentityCardResult;
import com.summ.tencentai.entity.result.TencentAiVehicleLicenseResult;

import java.util.List;

public interface ITencentAiView {

    void parseFailedResult(ResponseResult result);

    void parseVoiceResult(TencentAiASRResult result);

    void parseIdcardocrResult(TencentAiIdentityCardResult result);

    void parseVehicleLicense(List<TencentAiVehicleLicenseResult> list);

    void parseBusinessLicense(List<TencentAiBusinessLicenseResult> list);

    void parseCreditCard(List<TencentAiCreditCardResult> list);


}
