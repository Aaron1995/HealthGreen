package com.healthwalk.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table person.
 */
public class PersonBean {

    private Long id;
    private Integer user_id;
    private String date;
    private String time_begin;
    private String time_end;
    private String route;
    private String road;
    private String free;
    private String inhale;
    private String stay;

    public PersonBean() {
    }

    public PersonBean(Long id) {
        this.id = id;
    }

    public PersonBean(Long id, Integer user_id, String date, String time_begin, String time_end, String route, String road, String free, String inhale, String stay) {
        this.id = id;
        this.user_id = user_id;
        this.date = date;
        this.time_begin = time_begin;
        this.time_end = time_end;
        this.route = route;
        this.road = road;
        this.free = free;
        this.inhale = inhale;
        this.stay = stay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
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

}