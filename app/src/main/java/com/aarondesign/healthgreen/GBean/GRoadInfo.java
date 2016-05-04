package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2016/3/4 0004.
 */
public class GRoadInfo extends GId{

    public String name = "";
    public String direction="";
    public double start_x = 0;
    public double start_y = 0;
    public double end_x = 0;
    public double end_y = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getStart_x() {
        return start_x;
    }

    public void setStart_x(double start_x) {
        this.start_x = start_x;
    }

    public double getStart_y() {
        return start_y;
    }

    public void setStart_y(double start_y) {
        this.start_y = start_y;
    }

    public double getEnd_x() {
        return end_x;
    }

    public void setEnd_x(double end_x) {
        this.end_x = end_x;
    }

    public double getEnd_y() {
        return end_y;
    }

    public void setEnd_y(double end_y) {
        this.end_y = end_y;
    }

    @Override
    public String toString() {
        return "GRoadInfo{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", direction='" + direction + '\'' +
                ", start_x='" + start_x + '\'' +
                ", start_y='" + start_y + '\'' +
                ", end_x='" + end_x + '\'' +
                ", end_y='" + end_y + '\'' +
                '}';
    }
}
