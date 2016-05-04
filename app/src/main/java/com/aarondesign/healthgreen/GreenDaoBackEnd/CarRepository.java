package com.aarondesign.healthgreen.GreenDaoBackEnd;

import android.content.Context;
import android.util.Log;

import com.aarondesign.healthgreen.Application.MyApplication;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.healthwalk.bean.CarBean;
import com.healthwalk.bean.PersonBean;
import com.healthwalk.dao.CarBeanDao;
import com.healthwalk.dao.PersonBeanDao;

import java.util.List;

/**
 * Created by Aaron on 2016/3/15 0015.
 */
public class CarRepository {

    public static void insertOrUpdate(Context context, CarBean car) {
        getCarBeanDao(context).insertOrReplace(car);
    }

    public static void clearCar(Context context) {
        getCarBeanDao(context).deleteAll();
    }

    public static void deleteCarWithId(Context context, long id) {
        getCarBeanDao(context).delete(getCarBeanId(context, id));
    }

    public static double[] getExhaustAndGasolineWithDate(Context context, String date) {

        List instance = getCarBeanDao(context).queryBuilder()
                .where(CarBeanDao.Properties.Date.eq(date))
                .list();
        Log.d("PersonRepository", "instance.size()======" + instance.size());
        double[] stay = new double[2];
        if (instance.size() != 0) {
            List<CarBean> carList = getCarBeanDao(context).queryRaw("where date = ?", date);
            for (int i = 0; i < carList.size(); i++) {
                stay[0] += Double.parseDouble(carList.get(i).getExhaust());
                stay[1] += Double.parseDouble(carList.get(i).getGasoline());
                stay[0] = Math.round(stay[0] *100)/100.0;
                stay[1] = Math.round(stay[1] *100)/100.0;
                Log.d("CarRepository", "stay======" + stay[0] +"  "+stay [1]);
            }
        }
        return stay;
    }

    public static CarBean getCarBeanId(Context context, long id) {
        return getCarBeanDao(context).load(id);
    }

    public static List<CarBean> getAllCarBeans(Context context) {
        return getCarBeanDao(context).loadAll();
    }

    private static CarBeanDao getCarBeanDao(Context context) {
        return ((SysApplication) context.getApplicationContext()).getDaoSession().getCarBeanDao();
    }
}
