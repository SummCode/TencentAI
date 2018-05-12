package com.summ.tencentai.entity.result;

public class TencentAiASRResult {


    /**
     * format : 2
     * rate : 16000
     * text : 今天天气怎么样
     */

    private int format;
    private int rate;
    private String text;

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TencentAiASRResult{" +
                "format=" + format +
                ", rate=" + rate +
                ", text='" + text + '\'' +
                '}';
    }
}
