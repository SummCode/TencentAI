package com.summ.tencentai.mvp.presenter;

import com.summ.network.callback.HttpRequestCallback;
import com.summ.network.response.body.ResponseResult;
import com.summ.tencentai.entity.param.TencentAiASRParam;
import com.summ.tencentai.entity.param.TencentAiBusinessLicenseParam;
import com.summ.tencentai.entity.param.TencentAiCreditCardParam;
import com.summ.tencentai.entity.param.TencentAiIdentityCardParam;
import com.summ.tencentai.entity.param.TencentAiVehicleLicenseParam;
import com.summ.tencentai.entity.result.TencentAiASRResult;
import com.summ.tencentai.entity.result.TencentAiBaseResult;
import com.summ.tencentai.entity.result.TencentAiBusinessLicenseResult;
import com.summ.tencentai.entity.result.TencentAiCreditCardResult;
import com.summ.tencentai.entity.result.TencentAiIdentityCardResult;
import com.summ.tencentai.entity.result.TencentAiVehicleLicenseResult;
import com.summ.tencentai.mvp.model.impl.TencentAiModleImpl;
import com.summ.tencentai.mvp.view.ITencentAiView;
import com.summ.tool.LogUtils;

import java.util.List;
import java.util.Map;

public class TencentAiPersenter extends Presenter {

    private TencentAiModleImpl modle;
    private ITencentAiView view;

    public TencentAiPersenter(ITencentAiView view) {
        this.modle = new TencentAiModleImpl();
        this.view = view;
    }


    public void onParseVoice(TencentAiASRParam params, String appKey) {
        Map<String, Object> bodyParam = getBodyParams(params, appKey);
        modle.parseVoice(bodyParam, new HttpRequestCallback<TencentAiBaseResult>() {
            @Override
            public void onSuccess(TencentAiBaseResult tencentAiBaseResult) {

                if (tencentAiBaseResult.getRet() == 0) {
                    TencentAiASRResult result = (TencentAiASRResult) tencentAiBaseResult.getDataObject(TencentAiASRResult.class);
                    view.parseVoiceResult(result);
                } else {
                    onFailed(onConvert(tencentAiBaseResult));
                }
                LogUtils.d(tencentAiBaseResult.toString());
            }

            @Override
            public void onFailed(ResponseResult result) {
                view.parseFailedResult(result);
                LogUtils.e(result.toString());
            }
        });
    }


    public void onParseIdentityCard(TencentAiIdentityCardParam params, String appKey) {

        Map<String, Object> bodyParam = getBodyParams(params, appKey);

        modle.parseIdentityCard(bodyParam, new HttpRequestCallback<TencentAiBaseResult>() {
            @Override
            public void onSuccess(TencentAiBaseResult tencentAiBaseResult) {
                if (tencentAiBaseResult.getRet() == 0) {
                    TencentAiIdentityCardResult result = (TencentAiIdentityCardResult) tencentAiBaseResult.getDataObject(TencentAiIdentityCardResult.class);
                    view.parseIdcardocrResult(result);
                } else {
                    onFailed(onConvert(tencentAiBaseResult));
                }
                LogUtils.d(tencentAiBaseResult.toString());
            }

            @Override
            public void onFailed(ResponseResult result) {
                view.parseFailedResult(result);
                LogUtils.e(result.toString());
            }
        });
    }


    public void onParseVehicleLicense(TencentAiVehicleLicenseParam params, String appKey) {

        Map<String, Object> bodyParam = getBodyParams(params, appKey);

        modle.parseVehicleLicense(bodyParam, new HttpRequestCallback<TencentAiBaseResult>() {
            @Override
            public void onSuccess(TencentAiBaseResult tencentAiBaseResult) {
                if (tencentAiBaseResult.getRet() == 0) {
                    List<TencentAiVehicleLicenseResult> list = tencentAiBaseResult.getDataList(TencentAiVehicleLicenseResult.class);
                    if (list != null && list.size() > 0) {
                        view.parseVehicleLicense(list);
                    } else {
                        view.parseFailedResult(onConvert(tencentAiBaseResult));
                    }
                } else {
                    onFailed(onConvert(tencentAiBaseResult));
                }
                LogUtils.d(tencentAiBaseResult.toString());
            }

            @Override
            public void onFailed(ResponseResult result) {
                view.parseFailedResult(result);
                LogUtils.e(result.toString());
            }
        });
    }


    public void onParseBusinessLicense(TencentAiBusinessLicenseParam params, String appKey) {

        Map<String, Object> bodyParam = getBodyParams(params, appKey);

        modle.parseBusinessLicense(bodyParam, new HttpRequestCallback<TencentAiBaseResult>() {
            @Override
            public void onSuccess(TencentAiBaseResult tencentAiBaseResult) {

                if (tencentAiBaseResult.getRet() == 0) {
                    List<TencentAiBusinessLicenseResult> list = tencentAiBaseResult.getDataList(TencentAiBusinessLicenseResult.class);
                    if (list != null && list.size() > 0) {
                        view.parseBusinessLicense(list);
                    } else {
                        view.parseFailedResult(onConvert(tencentAiBaseResult));
                    }
                } else {
                    onFailed(onConvert(tencentAiBaseResult));
                }


                LogUtils.d(tencentAiBaseResult.toString());
            }

            @Override
            public void onFailed(ResponseResult result) {
                view.parseFailedResult(result);
                LogUtils.e(result.toString());
            }
        });
    }


    public void onParseCreditCard(TencentAiCreditCardParam params, String appKey) {

        Map<String, Object> bodyParam = getBodyParams(params, appKey);

        modle.parseCreditCard(bodyParam, new HttpRequestCallback<TencentAiBaseResult>() {
            @Override
            public void onSuccess(TencentAiBaseResult tencentAiBaseResult) {

                if (tencentAiBaseResult.getRet() == 0) {
                    List<TencentAiCreditCardResult> list = tencentAiBaseResult.getDataList(TencentAiCreditCardResult.class);
                    if (list != null && list.size() > 0) {
                        view.parseCreditCard(list);
                    } else {
                        view.parseFailedResult(onConvert(tencentAiBaseResult));
                    }
                } else {
                    onFailed(onConvert(tencentAiBaseResult));
                }

                LogUtils.d(tencentAiBaseResult.toString());
            }

            @Override
            public void onFailed(ResponseResult result) {
                view.parseFailedResult(result);
                LogUtils.e(result.toString());
            }
        });
    }

}
