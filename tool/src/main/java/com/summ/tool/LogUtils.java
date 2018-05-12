package com.summ.tool;

import android.text.TextUtils;
import android.util.Log;


/**
 * Describe：
 * <p>
 * Log输出控制类，发布时控制输出.
 * </p>
 * @author:summ.Chen.
 * Create：2016/10/15.
 * Email： summ_summ@163.com
 * Version：1.0.0
 */

public class LogUtils {


    public static String customTagPrefix = "Summ";

    public static boolean isPrintLog = true;

    private LogUtils() {
    }

    private LogUtils(String tag) {
        customTagPrefix = tag;
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.d(tag, content, tr);
        }

    }

    public static void e(Throwable throwable) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.e(tag, Log.getStackTraceString(throwable));
        }
    }

    public static void e(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.e(tag, content);
        }
    }


    public static void e(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.w(tag, tr);
        }
    }


    public static void wtf(String content) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (isPrintLog) {
            String tag = generateTag();
            Log.wtf(tag, tr);
        }
    }

}


