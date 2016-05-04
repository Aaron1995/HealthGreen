package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2015/12/28 0028.
 */
public class GCarKind extends GId {

    public int user_id = 0;
    public String kind = "";
    public String power = "";
    public String displacement = "";
    public String weight = "";

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    @Override
    public String toString() {
        return "GCarKind{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", kind='" + kind + '\'' +
                ", power='" + power + '\'' +
                ", displacement='" + displacement + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
