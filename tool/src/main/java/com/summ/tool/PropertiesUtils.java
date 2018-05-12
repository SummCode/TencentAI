package com.summ.tool;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author：Summ
 * @date：2018/4/21
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public class PropertiesUtils {

    public static Properties getProperties(Context context, String fileName) {
        Properties urlProps;
        Properties props = new Properties();
        try {
            //方法一：通过activity中的context攻取setting.properties的FileInputStream
            InputStream in = context.getAssets().open(fileName + ".properties");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        urlProps = props;
        return urlProps;
    }

}
