package com.aarondesign.healthgreen.ModifyView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Aaron on 2016/3/8 0008.
 */
public class TimePickerAlertDialog extends AlertDialog implements DialogInterface.OnClickListener{

    private TimePickerFrameLayout mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private OnDateTimeSetListener mOnDateTimeSetListener;

    public TimePickerAlertDialog(Context context, long date) {
        super(context);
        mDateTimePicker = new TimePickerFrameLayout(context);
        setView(mDateTimePicker);
        /*
         *实现接口，实现里面的方法
         */
        mDateTimePicker
                .setOnDateTimeChangedListener(new TimePickerFrameLayout.OnDateTimeChangedListener() {
                    @Override
                    public void onDateTimeChanged(TimePickerFrameLayout view,
                                                  int hour, int minute) {
                        mDate.set(Calendar.HOUR_OF_DAY, hour);
                        mDate.set(Calendar.MINUTE, minute);
                        mDate.set(Calendar.SECOND, 0);
                        /**
                         * 更新日期
                         */
                        updateTitle(mDate.getTimeInMillis());
                        Log.d("TPAD_update",hour+":"+minute);
                    }
                });
        setButton("设置", this);
        setButton2("取消", (OnClickListener) null);
        mDate.setTimeInMillis(date);
        updateTitle(mDate.getTimeInMillis());
    }
    /**
     * 接口回調
     * 控件 秒数
     */
    public interface OnDateTimeSetListener {
        void OnDateTimeSet(TimePickerAlertDialog dialog, long date);
    }

    /**
     * 更新对话框日期
     * @param date
     */
    private void updateTitle(long date) {
        int flag = DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;
        setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }
    /**
     * 对外公开方法让Activity实现
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
        mOnDateTimeSetListener = callBack;
    }

    public void onClick(DialogInterface arg0, int arg1) {
        if (mOnDateTimeSetListener != null) {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }

}
