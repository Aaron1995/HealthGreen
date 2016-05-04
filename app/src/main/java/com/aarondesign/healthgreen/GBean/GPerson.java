package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2015/12/29 0029.
 */
public class GPerson extends GId {

    public int user_id = 0;
    public String date = "";
    public String time_begin = "";
    public String time_end = "";
    public String route = "";
    public String road = "";
    public String free = "";
    public String inhale = "";
    public String stay = "";

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

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getInhale() {
        return inhale;
    }

    public void setInhale(String inhale) {
        this.inhale = inhale;
    }

    public String getStay() {
        return stay;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    @Override
    public String toString() {
        return "GPerson{" +
                "id=" + id +
                ", user_id=" + user_id + '\'' +
                ", date='" + date + '\'' +
                ", time_begin='" + time_begin + '\'' +
                ", time_end='" + time_end + '\'' +
                ", route='" + route + '\'' +
                ", road='" + road + '\'' +
                ", free='" + free + '\'' +
                ", inhale='" + inhale + '\'' +
                ", stay='" + stay + '\'' +
                '}';
    }
}
