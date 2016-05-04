package com.aarondesign.healthgreen.Bridge;

import android.graphics.Bitmap;

/**
 * Created by Aaron on 2015/11/9 0009.
 */
public class PersonBridge {

    private Bitmap btLocation;                  //定位图片
    private String placeBegin;                  //起点
    private String placeEnd;                    //终点
    private String month;                       //月份
    private String day;                         //日期
    private String timeBegin;                   //时间开始
    private String timeEnd;                     //时间结束
    private String personFreed;                 //环境产生
    private String personExhaust;               //个人吸入
    private String personStay;                  //残留体内

    public Bitmap getBtLocation() {
        return btLocation;
    }
    public void setBtLocation(Bitmap btLocation) {
        this.btLocation = btLocation;
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
    public String getPersonFreed() {
        return personFreed;
    }
    public void setPersonFreed(String personFreed) {
        this.personFreed = personFreed;
    }
    public String getPersonExhaust() {
        return personExhaust;
    }
    public void setPersonExhaust(String personExhaust) {
        this.personExhaust = personExhaust;
    }
    public String getPersonStay() {
        return personStay;
    }
    public void setPersonStay(String personStay) {
        this.personStay = personStay;
    }


}
