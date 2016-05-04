package com.healthwalk.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.healthwalk.bean.CarBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table car.
*/
public class CarBeanDao extends AbstractDao<CarBean, Long> {

    public static final String TABLENAME = "car";

    /**
     * Properties of entity CarBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property User_id = new Property(1, Integer.class, "user_id", false, "USER_ID");
        public final static Property Date = new Property(2, String.class, "date", false, "DATE");
        public final static Property Time_begin = new Property(3, String.class, "time_begin", false, "TIME_BEGIN");
        public final static Property Time_end = new Property(4, String.class, "time_end", false, "TIME_END");
        public final static Property Route = new Property(5, String.class, "route", false, "ROUTE");
        public final static Property Road = new Property(6, String.class, "road", false, "ROAD");
        public final static Property Exhaust = new Property(7, String.class, "exhaust", false, "EXHAUST");
        public final static Property Voc = new Property(8, String.class, "voc", false, "VOC");
        public final static Property Co = new Property(9, String.class, "co", false, "CO");
        public final static Property Pm = new Property(10, String.class, "pm", false, "PM");
        public final static Property Nox = new Property(11, String.class, "nox", false, "NOX");
        public final static Property Gasoline = new Property(12, String.class, "gasoline", false, "GASOLINE");
        public final static Property Carkind_id = new Property(13, Integer.class, "carkind_id", false, "CARKIND_ID");
        public final static Property Drive = new Property(14, Integer.class, "drive", false, "DRIVE");
    };


    public CarBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CarBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'car' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'USER_ID' INTEGER," + // 1: user_id
                "'DATE' TEXT," + // 2: date
                "'TIME_BEGIN' TEXT," + // 3: time_begin
                "'TIME_END' TEXT," + // 4: time_end
                "'ROUTE' TEXT," + // 5: route
                "'ROAD' TEXT," + // 6: road
                "'EXHAUST' TEXT," + // 7: exhaust
                "'VOC' TEXT," + // 8: voc
                "'CO' TEXT," + // 9: co
                "'PM' TEXT," + // 10: pm
                "'NOX' TEXT," + // 11: nox
                "'GASOLINE' TEXT," + // 12: gasoline
                "'CARKIND_ID' INTEGER," + // 13: carkind_id
                "'DRIVE' INTEGER);"); // 14: drive
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_car_ID ON car" +
                " (ID);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'car'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CarBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(3, date);
        }
 
        String time_begin = entity.getTime_begin();
        if (time_begin != null) {
            stmt.bindString(4, time_begin);
        }
 
        String time_end = entity.getTime_end();
        if (time_end != null) {
            stmt.bindString(5, time_end);
        }
 
        String route = entity.getRoute();
        if (route != null) {
            stmt.bindString(6, route);
        }
 
        String road = entity.getRoad();
        if (road != null) {
            stmt.bindString(7, road);
        }
 
        String exhaust = entity.getExhaust();
        if (exhaust != null) {
            stmt.bindString(8, exhaust);
        }
 
        String voc = entity.getVoc();
        if (voc != null) {
            stmt.bindString(9, voc);
        }
 
        String co = entity.getCo();
        if (co != null) {
            stmt.bindString(10, co);
        }
 
        String pm = entity.getPm();
        if (pm != null) {
            stmt.bindString(11, pm);
        }
 
        String nox = entity.getNox();
        if (nox != null) {
            stmt.bindString(12, nox);
        }
 
        String gasoline = entity.getGasoline();
        if (gasoline != null) {
            stmt.bindString(13, gasoline);
        }
 
        Integer carkind_id = entity.getCarkind_id();
        if (carkind_id != null) {
            stmt.bindLong(14, carkind_id);
        }
 
        Integer drive = entity.getDrive();
        if (drive != null) {
            stmt.bindLong(15, drive);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CarBean readEntity(Cursor cursor, int offset) {
        CarBean entity = new CarBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // date
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // time_begin
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // time_end
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // route
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // road
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // exhaust
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // voc
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // co
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // pm
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // nox
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // gasoline
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // carkind_id
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14) // drive
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CarBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime_begin(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTime_end(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRoute(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRoad(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setExhaust(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setVoc(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCo(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPm(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setNox(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setGasoline(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCarkind_id(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setDrive(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CarBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CarBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}