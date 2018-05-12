package com.summ.network;

import android.content.Context;

import com.summ.network.request.RequestConfig;

/**
 * @author：Summ
 * @date：2018/4/22
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public class NetworkConfig {

    public static void init(Context context){
        RequestConfig requestConfig = new RequestConfig();
        requestConfig.initConfig(context);
    }


}
