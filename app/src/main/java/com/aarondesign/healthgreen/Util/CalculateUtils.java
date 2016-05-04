package com.aarondesign.healthgreen.Util;

/**
 * Created by Aaron on 2016/3/24 0024.
 */
public class CalculateUtils {

    private double a, b;

    public CalculateUtils(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public String getCigagettesNum() {
        return "相当于您吸了" + Math.round(a / 8.23) + "根烟！";
    }

    public String getTreesNum() {
        return "相当于您砍了" + Math.round(a / 15.67) + "课树！";
    }

}
