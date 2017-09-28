package com.mcms.pda.ui.adapter;

/**
 * Created by chenjiaming1 on 2017/9/27.
 */

public class Item {
    private int res;//图片
    private String tv1;//文字
    private int type;//类型

    public Item(int res, String tv1,int type) {
        this.res = res;
        this.tv1 = tv1;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTv1() {
        return tv1;
    }

    public void setTv1(String tv1) {
        this.tv1 = tv1;
    }

}
