package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GreenDaoBackEnd.CarRepository;
import com.aarondesign.healthgreen.GreenDaoBackEnd.PersonRepository;
import com.aarondesign.healthgreen.ModifyView.TimePickerAlertDialog;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Util.CalculateUtils;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.Inflater;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Home extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private static final int DIALOG_CIGARETTE = 1;
    private static final int DIALOG_TREE = 2;

    private RelativeLayout homeRLUser;
    private RelativeLayout homeRLCar;
    private RelativeLayout homeRLTime;
    private Date date;

    private GestureDetector detector;

    private ImageView homeIVMap;
    private ImageView homeIVPerson;
    private ImageView timeToYesterday;
    private ImageView timeToTomorrow;
    private Intent translateUser;
    private Intent translateCar;
    private Intent translateMap;
    private Intent translatePerson;
    private TextView tMonthAndDay;
    private TextView tYear;
    private TextView tInhale;
    private TextView tStay;
    private TextView tExhaust;
    private TextView tGasoline;

    private double dInhale;
    private double dStay;
    private double dExhaust;
    private double dGasoline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SysApplication.getInstance().addActivity(this);

        init();
        findView();
        initEvent();
        getStayWithDate();
    }

    private void init() {
        detector = new GestureDetector(this, this);
        date = new Date();
    }

    private void findView() {
        homeRLUser = (RelativeLayout) findViewById(R.id.home_rl_user);
        homeRLCar = (RelativeLayout) findViewById(R.id.home_rl_car);
        homeIVMap = (ImageView) findViewById(R.id.home_iv_map);
        homeIVPerson = (ImageView) findViewById(R.id.home_iv_person);
        homeRLTime = (RelativeLayout) findViewById(R.id.home_rl_date);
        timeToYesterday = (ImageView) findViewById(R.id.home_iv_left_time);
        timeToTomorrow = (ImageView) findViewById(R.id.home_iv_right_time);
        tMonthAndDay = (TextView) findViewById(R.id.home_tv_month_day);
        tYear = (TextView) findViewById(R.id.home_tv_year);
        tInhale = (TextView) findViewById(R.id.home_tv_inhale_num);
        tStay = (TextView) findViewById(R.id.home_tv_stay_num);
        tExhaust = (TextView) findViewById(R.id.home_tv_exhaust_num);
        tGasoline = (TextView) findViewById(R.id.home_tv_gasoline_num);
    }

    private void initEvent() {
        homeRLUser.setOnClickListener(this);
        homeRLCar.setOnClickListener(this);
        homeIVMap.setOnClickListener(this);
        homeIVPerson.setOnClickListener(this);
        timeToYesterday.setOnClickListener(this);
        timeToTomorrow.setOnClickListener(this);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        tMonthAndDay.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        tYear.setText(calendar.get(Calendar.YEAR) + "");

    }

    private void dateSelect() {
        TimePickerAlertDialog dialog = new TimePickerAlertDialog(this, System.currentTimeMillis());
    }

    /**
     * 重写返回事件
     *
     * @param keyCode 按键类型
     * @param event   产生的事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage("确定要退出么？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭整个程序
                            SysApplication.getInstance().exit();
                            System.exit(0);
                        }
                    }).create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dialogShow(double a, double b, int dialog_style) {
        final Dialog dialog = new Dialog(this, R.style.home_dialog);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_home, null);
        dialog.setContentView(view);
        TextView message = (TextView) view.findViewById(R.id.dialog_message);
        CalculateUtils calculateUtils = new CalculateUtils(a, b);
        if (DIALOG_CIGARETTE == dialog_style)
            message.setText(calculateUtils.getCigagettesNum());
        else if (DIALOG_TREE == dialog_style)
            message.setText(calculateUtils.getTreesNum());
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_rl_user:
                dialogShow(dInhale, dStay, DIALOG_CIGARETTE);
                break;
            case R.id.home_rl_car:
                dialogShow(dExhaust, dGasoline, DIALOG_TREE);
                break;
            case R.id.home_iv_map:
                translate(Configs.TRANSLATE_MAP);
                break;
            case R.id.home_iv_person:
                translate(Configs.TRANSLATE_PERSON);
                break;
            case R.id.home_rl_date:
                dateSelect();
                break;
            case R.id.home_iv_left_time:
                timeModify(timeToYesterday);
                getStayWithDate();
                break;
            case R.id.home_iv_right_time:
                timeModify(timeToTomorrow);
                getStayWithDate();
                break;
        }
    }

    private void timeModify(View view) {
        Format format = new SimpleDateFormat("MM月dd日");
        try {
            Calendar calendar = Calendar.getInstance();
            GregorianCalendar gc = new GregorianCalendar();
            Date date = (Date) format.parseObject(tMonthAndDay.getText().toString());
            calendar.setTime(date);
            gc.setTime(date);
            if (view == timeToYesterday) {
                gc.add(GregorianCalendar.DAY_OF_MONTH, -1);
                tMonthAndDay.setText((gc.get(GregorianCalendar.MONTH) + 1) + "月" + gc.get(GregorianCalendar.DAY_OF_MONTH) + "日");
            } else if (view == timeToTomorrow) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);//让日期加1
                tMonthAndDay.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStayWithDate() {
        Calendar calendar = Calendar.getInstance();
        Format format = new SimpleDateFormat("MM月dd日");
        String dateStr;
        try {
            Date date = (Date) format.parseObject(tMonthAndDay.getText().toString());
            calendar.setTime(date);
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            if (month.length() == 1)
                month = "0" + month;
            dateStr = tYear.getText().toString() + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("Home", "dateStr======" + dateStr);
            double[] person = PersonRepository.getStayAndInhaleWithDate(this, dateStr);
            double[] car = CarRepository.getExhaustAndGasolineWithDate(this, dateStr);
            if (person != null) {
                dInhale = person[1];
                dStay = person[0];
                tInhale.setText(dInhale + "mg");
                tStay.setText(dStay + "mg");
            }
            if (car != null) {
                dExhaust = car[0];
                dGasoline = car[1];
                tExhaust.setText(dExhaust + "mg");
                tGasoline.setText(dGasoline + "mL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() > 60 && Math.abs(velocityX) > 10)
            translate(Configs.TRANSLATE_USER);
        else if (e1.getX() - e2.getX() > 60 && Math.abs(velocityX) > 10)
            translate(Configs.TRANSLATE_CAR);
        else if (e1.getY() - e2.getY() > 20 && Math.abs(velocityY) > 0)
            translate(Configs.TRANSLATE_MAP);
        return false;
    }

    public void translate(int position) {
        if (Configs.TRANSLATE_USER == position) {
            if (null == translateUser)
                translateUser = new Intent(this, Person.class);
            else {
                translateUser.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateUser.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateUser);
            overridePendingTransition(R.anim.ts_horizontal1, R.anim.ts_horizontal2);
        } else if (Configs.TRANSLATE_CAR == position) {
            if (null == translateCar)
                translateCar = new Intent(this, Car.class);
            else {
                translateCar.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateCar.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateCar);
            overridePendingTransition(R.anim.ts_horizontal3, R.anim.ts_horizontal4);
        } else if (Configs.TRANSLATE_MAP == position) {
            if (null == translateMap)
                translateMap = new Intent(this, Map.class);
            else {
                translateMap.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateMap.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateMap);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        } else if (Configs.TRANSLATE_PERSON == position) {
            if (null == translatePerson)
                translatePerson = new Intent(this, User.class);
            else {
                translatePerson.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translatePerson.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translatePerson);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }
}
