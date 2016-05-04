package com.aarondesign.healthgreen.GBean;

/**
 * Created by Aaron on 2016/3/7 0007.
 */
public class GExhaustDetails {
    public double co = 0;
    public double nox = 0;
    public double voc = 0;
    public double pm = 0;
    public double gasoline = 0;
    public double exhaust = 0;

    public double getCo() {
        return co;
    }

    public void setCo(double co) {
        this.co = co;
    }

    public double getNox() {
        return nox;
    }

    public void setNox(double nox) {
        this.nox = nox;
    }

    public double getVoc() {
        return voc;
    }

    public void setVoc(double voc) {
        this.voc = voc;
    }

    public double getPm() {
        return pm;
    }

    public void setPm(double pm) {
        this.pm = pm;
    }

    public double getGasoline() {
        return gasoline;
    }

    public void setGasoline(double gasoline) {
        this.gasoline = gasoline;
    }

    public double getExhaust() {
        return exhaust;
    }

    public void setExhaust(double exhaust) {
        this.exhaust = exhaust;
    }

    @Override
    public String toString() {
        return "GExhaustDetails{" +
                "exhaust=" + exhaust +
                ", gasoline=" + gasoline +
                ", pm=" + pm +
                ", voc=" + voc +
                ", nox=" + nox +
                ", co=" + co +
                '}';
    }
}
