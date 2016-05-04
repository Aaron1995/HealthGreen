package com.aarondesign.healthgreen.GreenDaoBackEnd;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aarondesign.healthgreen.Acitivitys.Person;
import com.aarondesign.healthgreen.Application.MyApplication;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.healthwalk.bean.PersonBean;
import com.healthwalk.dao.PersonBeanDao;

import java.util.List;

/**
 * Created by Aaron on 2016/3/15 0015.
 */
public class PersonRepository {

    public static void insertOrUpdate(Context context, PersonBean person) {
        getPersonBeanDao(context).insertOrReplace(person);
    }

    public static void clearPerson(Context context) {
        getPersonBeanDao(context).deleteAll();
    }

    public static void deletePersonWithId(Context context, long id) {
        getPersonBeanDao(context).delete(getPersonBeanId(context, id));
    }

    public static double[] getStayAndInhaleWithDate(Context context, String date) {

        List instance = getPersonBeanDao(context).queryBuilder()
                .where(PersonBeanDao.Properties.Date.eq(date))
                .list();
        Log.d("PersonRepository", "instance.size()======" + instance.size());
        double[] stay = new double[2];
        if (instance.size() != 0) {
            List<PersonBean> personList = getPersonBeanDao(context).queryRaw("where date = ?", date);
            for (int i = 0; i < personList.size(); i++) {
                stay[0] += Double.parseDouble(personList.get(i).getStay());
                stay[1] += Double.parseDouble(personList.get(i).getInhale());
                stay[0] = Math.round(stay[0] *100)/100.0;
                stay[1] = Math.round(stay[1] *100)/100.0;
                Log.d("PersonRepository", "stay======" + stay[0] +"  "+stay [1]);
            }
        }
        return stay;
    }

    public static PersonBean getPersonBeanId(Context context, long id) {
        return getPersonBeanDao(context).load(id);
    }

    public static List<PersonBean> getAllPersonBeans(Context context) {
        return getPersonBeanDao(context).loadAll();
    }

    private static PersonBeanDao getPersonBeanDao(Context context) {
        return ((SysApplication) context.getApplicationContext()).getDaoSession().getPersonBeanDao();
    }
}
