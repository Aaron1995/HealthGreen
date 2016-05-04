package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2015/12/28 0028.
 */
public class GUser extends GId {

    public String phone = "";
    public String password = "";
    public String name = "";
    public int gender = 1;
    public String weight = "";
    public String height = "";
    public String age = "";
    public int carkind_id = 2;
    public String fvc = "";
    public String city = "";
    public String register_datetime = "";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getCarkind_id() {
        return carkind_id;
    }

    public void setCarkind_id(int carkind_id) {
        this.carkind_id = carkind_id;
    }

    public String getFvc() {
        return fvc;
    }

    public void setFvc(String fvc) {
        this.fvc = fvc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegister_datetime() {
        return register_datetime;
    }

    public void setRegister_datetime(String register_datetime) {
        this.register_datetime = register_datetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", weight='" + weight + '\'' +
                ", height='" + height + '\'' +
                ", age='" + age + '\'' +
                ", carkind_id=" + carkind_id +
                ", fvc='" + fvc + '\'' +
                ", city='" + city + '\'' +
                ", register_datetime='" + register_datetime + '\'' +
                '}';
    }

}
