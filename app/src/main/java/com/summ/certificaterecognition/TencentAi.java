package com.summ.certificaterecognition;

import android.app.Application;

import com.summ.network.NetworkConfig;
import com.summ.tool.CrashReportUtils;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author：Summ
 * @date：2018/5/18
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public class TencentAi extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReportUtils.initCrashReport(getApplicationContext());
        NetworkConfig.init(this);
    }
}
