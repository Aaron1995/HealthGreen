package com.aarondesign.healthgreen;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Aaron on 2015/11/10 0010.
 */
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (null == instance)
            instance = new ActivityManager();
        return instance;
    }

    //退出栈顶Acitivity
    public void popActivity(Activity activity) {
        if (null != activity) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    //获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    //将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (null == activityStack)
            activityStack = new Stack<>();
        activityStack.add(activity);
    }

    //退出栈中所有Activity
    public void popAllActivityExcepOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (null == activity)
                break;
            if (activity.getClass().equals(cls))
                break;
            popActivity(activity);
        }
    }
}
