package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2015/12/30 0030.
 */
public class GCar extends GId {
    public int user_id = 0;
    public String date = "";
    public String time_begin = "";
    public String time_end = "";
    public String route = "";
    public String road = "";
    public String exhaust = "";
    public String voc = "";
    public String co = "";
    public String nox = "";
    public String pm = "";
    public String gasoline = "";
    public int drive = 0;
    public int carkind_id = 0;

    public int getDrive() {
        return drive;
    }

    public void setDrive(int drive) {
        this.drive = drive;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getExhaust() {
        return exhaust;
    }

    public void setExhaust(String exhaust) {
        this.exhaust = exhaust;
    }

    public String getVoc() {
        return voc;
    }

    public void setVoc(String voc) {
        this.voc = voc;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getNox() {
        return nox;
    }

    public void setNox(String nox) {
        this.nox = nox;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getGasoline() {
        return gasoline;
    }

    public void setGasoline(String gasoline) {
        this.gasoline = gasoline;
    }

    public int getCarkind_id() {
        return carkind_id;
    }

    public void setCarkind_id(int carkind_id) {
        this.carkind_id = carkind_id;
    }

    @Override
    public String toString() {
        return "GCar{" +
                "id=" + id +
                ", user_id=" + user_id + '\'' +
                ", date='" + date + '\'' +
                ", time_begin='" + time_begin + '\'' +
                ", time_end='" + time_end + '\'' +
                ", route='" + route + '\'' +
                ", road='" + road + '\'' +
                ", exhaust='" + exhaust + '\'' +
                ", voc='" + voc + '\'' +
                ", co='" + co + '\'' +
                ", nox='" + nox + '\'' +
                ", pm='" + pm + '\'' +
                ", gasoline='" + gasoline + '\'' +
                ", carkind_id=" + carkind_id + '\'' +
                ", driver=" + drive +
                '}';
    }
}
