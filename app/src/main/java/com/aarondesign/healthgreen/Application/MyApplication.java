package com.aarondesign.healthgreen.Application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.healthwalk.dao.DaoMaster;
import com.healthwalk.dao.DaoSession;

/**
 * Created by Aaron on 2016/3/15 0015.
 */
public class MyApplication extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "HealthWalk_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
