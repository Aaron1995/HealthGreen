package com.aarondesign.healthgreen.Bridge;

import android.graphics.Bitmap;

/**
 * Created by Aaron on 2015/11/17 0017.
 */
public class CarBridge {

    private Bitmap btCarKind;                  //行驶车辆图片
    private Bitmap exhaustTrend;                //尾气趋势图片
    private Bitmap gasolineTrend;               //汽油趋势图片
    private Bitmap carModify;                   //修改车记录图片

    private String placeBegin;                  //起点
    private String placeEnd;                    //终点
    private String month;                       //月份
    private String day;                         //日期
    private String timeBegin;                   //时间开始
    private String timeEnd;                     //时间结束
    private String carExhaust;                  //车尾气
    private String carGasoline;                 //车耗油

    public Bitmap getBtCarKind() {
        return btCarKind;
    }
    public void setBtCarKind(Bitmap btCarKind) {
        this.btCarKind = btCarKind;
    }
    public Bitmap getExhaustTrend(){
        return exhaustTrend;
    }
    public void setExhaustTrend(Bitmap exhaustTrend){
        this.exhaustTrend = exhaustTrend;
    }
    public Bitmap getGasolineTrend(){
        return gasolineTrend;
    }
    public void setGasolineTrend(Bitmap gasolineTrend){
        this.gasolineTrend = gasolineTrend;
    }
    public String getPlaceBegin() {
        return placeBegin;
    }
    public void setPlaceBegin(String placeBegin) {
        this.placeBegin = placeBegin;
    }
    public String getPlaceEnd() {
        return placeEnd;
    }
    public void setPlaceEnd(String placeEnd) {
        this.placeEnd = placeEnd;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getTimeBegin() {
        return timeBegin;
    }
    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }
    public String getTimeEnd() {
        return timeEnd;
    }
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
    public String getCarExhaust() {
        return carExhaust;
    }
    public void setCarExhaust(String carExhaust) {
        this.carExhaust = carExhaust;
    }
    public String getCarGasoline() {
        return carGasoline;
    }
    public void setCarGasoline(String carGasoline) {
        this.carGasoline = carGasoline;
    }

}
