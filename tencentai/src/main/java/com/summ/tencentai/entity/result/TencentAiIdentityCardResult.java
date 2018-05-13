package com.summ.tencentai.entity.result;

import java.util.List;

public class TencentAiIdentityCardResult implements TencentAiPrint {

    /**
     * name : 李明
     * sex : 男
     * nation : 汉
     * birth : 1987/1/1
     * address : 北京市石景山区高新技术园腾讯大楼
     * id : 440524198701010014
     * frontimage : /9j/...
     * authority :
     * valid_date :
     * backimage :
     */

    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String id;
    private String frontimage;
    private String authority;
    private String valid_date;
    private String backimage;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation == null ? "" : nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth == null ? "" : birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontimage() {
        return frontimage == null ? "" : frontimage;
    }

    public void setFrontimage(String frontimage) {
        this.frontimage = frontimage;
    }

    public String getAuthority() {
        return authority == null ? "" : authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getValid_date() {
        return valid_date == null ? "" : valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getBackimage() {
        return backimage == null ? "" : backimage;
    }

    public void setBackimage(String backimage) {
        this.backimage = backimage;
    }

    @Override
    public String toString() {
        return "TencentAiIdentityCardResult{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nation='" + nation + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", frontimage='" + frontimage + '\'' +
                ", authority='" + authority + '\'' +
                ", valid_date='" + valid_date + '\'' +
                ", backimage='" + backimage + '\'' +
                '}';
    }

    @Override
    public String onShowContent() {
        return "姓名：" + getName() + '\n' +
                "性别：" + getSex() + '\n' +
                "民族：" + getNation() + '\n' +
                "出生：" + getBirth() + '\n' +
                "住址：" + getAddress() + '\n' +
                "身份证号：" + getId() + '\n' +
                "发证机关：" + getAuthority() + '\n' +
                "有效日期：" + getValid_date();
    }


}
