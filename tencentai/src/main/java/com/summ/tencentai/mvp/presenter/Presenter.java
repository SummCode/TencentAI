package com.summ.tencentai.mvp.presenter;

import android.support.annotation.NonNull;

import com.summ.network.response.body.ResponseResult;
import com.summ.tencentai.entity.param.BaseTencentAiParam;
import com.summ.tencentai.entity.param.TencentAiASRParam;
import com.summ.tencentai.entity.param.TencentAiIdentityCardParam;
import com.summ.tencentai.entity.result.TencentAiBaseResult;

import java.util.Map;

public class Presenter implements IBasePresenter<TencentAiBaseResult> {

    @Override
    public ResponseResult onConvert(TencentAiBaseResult result) {
        String url = "";
        int code = result.getRet();
        String msg = result.getMsg();
        return new ResponseResult(url, code, msg);
    }

    @NonNull
    public Map<String, Object> getBodyParams(BaseTencentAiParam params, String appKey) {
        Map<String, Object> bodyParam = params.getRequestMap();
        String signInfo = params.getReqSign(bodyParam, appKey);
        bodyParam.put("sign", signInfo);
        return bodyParam;
    }

}
