package com.summ.tencentai.entity.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 行驶、驾驶、营业执照、银行卡
 */
public class TencentAiCreditCardResult implements TencentAiPrint{


    /**
     * item : 车牌号码
     * itemstring : 沪AA1234
     * itemcoord : [{"x":213,"y":124,"width":110,"height":39}]
     * itemconf : 0.939449
     */

    private String item;
    private String itemstring;
    private double itemconf;
    private List<TencentAiCoordResult> itemcoord;

    public String getItem() {
        return item == null ? "" : item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemstring() {
        return itemstring == null ? "" : itemstring;
    }

    public void setItemstring(String itemstring) {
        this.itemstring = itemstring;
    }

    public double getItemconf() {
        return itemconf;
    }

    public void setItemconf(double itemconf) {
        this.itemconf = itemconf;
    }

    public List<TencentAiCoordResult> getItemcoord() {
        return itemcoord == null ? new ArrayList<TencentAiCoordResult>() : itemcoord;
    }

    public void setItemcoord(List<TencentAiCoordResult> itemcoord) {
        this.itemcoord = itemcoord;
    }

    @Override
    public String onShowContent() {
        return getItem() + "：" + getItemstring() + '\n';
    }


    @Override
    public String toString() {
        return "TencentCommonPapersResult{" +
                "item='" + item + '\'' +
                ", itemstring='" + itemstring + '\'' +
                ", itemconf=" + itemconf +
                ", itemcoord=" + itemcoord +
                '}';
    }
}

