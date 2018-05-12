package com.summ.tencentai;


import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class StringUtilsTest {

    @Test
    public void test_getRandomStr() {

        try {
            System.out.println(URLEncoder.encode("陈玉龙", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
