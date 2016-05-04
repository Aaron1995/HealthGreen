package com.aarondesign.healthgreen.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.aarondesign.healthgreen.Static.Configs;

/**
 * Created by Aaron on 2015/12/22 0022.
 */
public class MySharedPreferencesUtils {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String FILE_NAME = "file_user";

    /**
     * 存入数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void save(Context context, String key, Object value) {

        if (null == sharedPreferences) {
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        if (null == editor) {
            editor = sharedPreferences.edit();
        }

        // 判断value的类型,并将value存入FILE_NAME文件内
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        editor.commit();
        editor = null;
        sharedPreferences = null;
    }

    /**
     * 从sharedpreferences里面获取数据
     *
     * @param context
     * @param key
     * @param valueType
     * @return
     */
    public static Object get(Context context, String key, int valueType) {
        Object result = null;
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Activity.MODE_WORLD_READABLE);

        switch (valueType) {
            case Configs.TYPE_INTEGER:
                result = sharedPreferences.getInt(key, -1);
                break;
            case Configs.TYPE_STRING:
                result = sharedPreferences.getString(key, "");
                break;
            case Configs.TYPE_FLOAT:
                result = sharedPreferences.getFloat(key, -1);
                break;
            case Configs.TYPE_LONG:
                result = sharedPreferences.getLong(key, -1);
                break;
            case Configs.TYPE_BOOLEAN:
                result = sharedPreferences.getBoolean(key, false);
                break;
        }
        sharedPreferences = null;
        return result;

    }

}
