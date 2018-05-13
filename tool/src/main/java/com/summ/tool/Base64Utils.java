package com.summ.tool;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author：Summ
 * @date：2018/4/19
 * @email： summ_summ@163.com
 * version：1.0.0
 * <p>
 * describe：
 * <p>
 * <p>
 */
public class Base64Utils {


    /**
     * encodeBase64File:(将文件转成base64 字符串).
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @author aliyunzixun@xxx.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        return encodeBase64File(file);
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串).
     *
     * @param file 文件
     * @return
     * @throws Exception
     * @author aliyunzixun@xxx.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(File file) {
        byte[] buffer = null;
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件).
     *
     * @param base64Code 编码后的字串
     * @param savePath   文件保存路径
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String savePath) {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        try {
            FileOutputStream out = new FileOutputStream(savePath);
            out.write(buffer);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
