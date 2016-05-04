package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GBean.GExhaustDetails;
import com.aarondesign.healthgreen.ModifyView.TimePickerAlertDialog;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarAddConfig;
import com.aarondesign.healthgreen.Static.CarConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.TransitionsUtil;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Aaron on 2015/11/8 0008.
 */
public class CarAdd extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener {

    public static final MediaType MEDIA_TYPE_HTML
            = MediaType.parse("text/html;charset=UTF-8");

    private GestureDetector detector;

    private ToggleButton isCarHave;
    private View carLine;
    private LinearLayout carSetting;
    private Button timeStart;
    private Button timeEnd;
    private EditText routeStart;
    private EditText routeEnd;
    private Spinner carKind;
    private Button carAddYes;
    private Intent translateBack;

    private String timeStartStr = "";
    private String routeStartStr = "1";
    private String routeEndStr = "2";
    private String timeEndStr = "";
    private LatLng placeStart = null;
    private LatLng placeEnd = null;
    private String status;
    private int carkind_id;
    private int drive_status = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Configs.CONNECTION_SUCCESS:
                    Log.d("CarAdd", "handle=成功获取服务器数据");
                    GExhaustDetails exhaustDetails = (GExhaustDetails) msg.obj;
                    uploadCarDatas(exhaustDetails);
                    break;
                case Configs.CONNECTION_FAILURE:
                    Log.d("CarAdd", "handle=失败获取服务器数据");
                    break;
                case Configs.CONNECTION_ERROR:
                    Log.d("CarAdd", "handle=错误获取服务器数据");
                    break;
            }
        }
    };

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Configs.CONNECTION_SUCCESS:
                    Log.d("CarAdd", "handle1=成功获取服务器数据");
//                    translate(Configs.TRANSLATE_BACK);
                    Toast.makeText(CarAdd.this,"更新记录成功！",Toast.LENGTH_SHORT).show();
                    TransitionsUtil.TranslateOfActivity(CarAdd.this,HomeNew.class,Configs.TRANSLATE_STYLE_MAIN_BACK);
                    break;
                case Configs.CONNECTION_FAILURE:
                    Toast.makeText(CarAdd.this,"更新记录失败！",Toast.LENGTH_SHORT).show();
                    Log.d("CarAdd", "handle1=失败获取服务器数据");
                    break;
                case Configs.CONNECTION_ERROR:
                    Log.d("CarAdd", "handle1=错误获取服务器数据");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);
        SysApplication.getInstance().addActivity(this);

        init();
        findView();
        initEvent();
        getMyShare();
    }

    private void init() {

        int hour = new Date().getHours();
        int minute = new Date().getMinutes();
        String minuteStr = minute + "";
        if (minute < 10)
            minuteStr = "0" + minute;
        timeStartStr = hour + ":" + minuteStr;
        timeEndStr = (hour + 1) + ":" + minuteStr;
        if (null == detector)
            detector = new GestureDetector(this, this);
    }

    private void findView() {

        timeStart = (Button) findViewById(R.id.car_add_begin_tp);
        timeEnd = (Button) findViewById(R.id.car_add_end_tp);
        routeStart = (EditText) findViewById(R.id.car_add_begin_route);
        routeEnd = (EditText) findViewById(R.id.car_add_end_route);
        carLine = findViewById(R.id.car_add_line);
        carSetting = (LinearLayout) findViewById(R.id.car_add_have);
        isCarHave = (ToggleButton) findViewById(R.id.car_add_car_self);
        isCarHave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isCarHave.isChecked()) {
                    isCarHave.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_btn_l_slip));
                    carLine.setVisibility(View.VISIBLE);
                    carSetting.setVisibility(View.VISIBLE);
                    drive_status = 0;
                } else {
                    isCarHave.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_btn_r_slip));
                    carLine.setVisibility(View.INVISIBLE);
                    carSetting.setVisibility(View.INVISIBLE);
                    drive_status = 1;
                }
            }
        });
        carKind = (Spinner) findViewById(R.id.car_add_spinner_car_kind);
        carKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carkind_id = position + 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                carkind_id = (int) MySharedPreferencesUtils.get(CarAdd.this, UserConfig.USER_CARKIND_ID, Configs.TYPE_INTEGER);
            }
        });
        carAddYes = (Button) findViewById(R.id.car_add_iv_yes);
    }

    private void initEvent() {
        timeStart.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        routeStart.setOnClickListener(this);
        routeEnd.setOnClickListener(this);

        timeStart.setText(timeStartStr);
        timeEnd.setText(timeEndStr);
        if (!"1".equals(routeStartStr))
            routeStart.setText(routeStartStr);
        if (!"2".equals(routeEndStr))
            routeEnd.setText(routeEndStr);
        carAddYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_add_begin_tp:
                showDialog(timeStartStr);
                break;
            case R.id.car_add_end_tp:
                showDialog(timeEndStr);
                break;
            case R.id.car_add_begin_route:
                status = routeStartStr;
//                TransitionsUtil.TranslateOfActivity(this, MapCarAdd.class, Configs.TRANSLATE_STYLE_POP);
                translate(Configs.TRANSLATE_CAR_ADD_MAP);
                break;
            case R.id.car_add_end_route:
                status = routeEndStr;
                translate(Configs.TRANSLATE_CAR_ADD_MAP);
                break;
            case R.id.car_add_iv_yes:
                if ("1".equals(routeStartStr)) {
                    Toast.makeText(this, "请在地图上选取起点", Toast.LENGTH_SHORT).show();
                } else if ("2".equals(routeEndStr)) {
                    Toast.makeText(this, "请在地图上选取终点", Toast.LENGTH_SHORT).show();
                } else {
                    getExhaustDetails(getTime(), getDistance(), carkind_id);
                }
                break;
        }
    }

    public void showDialog(final String str) {
        TimePickerAlertDialog dialog = new TimePickerAlertDialog(this,
                System.currentTimeMillis());
        /**
         * 实现接口
         */
        dialog.setOnDateTimeSetListener(new TimePickerAlertDialog.OnDateTimeSetListener() {
            public void OnDateTimeSet(TimePickerAlertDialog dialog, long date) {
                if (timeStartStr.equals(str)) {
                    timeStartStr = getStringDate(date);
                    MySharedPreferencesUtils.save(CarAdd.this, CarAddConfig.TRANSMIT_TIME_START_STATUS, CarAddConfig.TRANSMIT_TIME_START);
                    MySharedPreferencesUtils.save(CarAdd.this, CarAddConfig.TIME_START_MAP, timeStartStr);
                    timeStart.setText(timeStartStr);
                } else if (timeEndStr.equals(str)) {
                    timeEndStr = getStringDate(date);
                    MySharedPreferencesUtils.save(CarAdd.this, CarAddConfig.TRANSMIT_TIME_END_STARTS, CarAddConfig.TRANSMIT_TIME_END);
                    MySharedPreferencesUtils.save(CarAdd.this, CarAddConfig.TIME_END_MAP, timeEndStr);
                    timeEnd.setText(timeEndStr);
                }
                Toast.makeText(CarAdd.this,
                        "您输入的日期是：" + getStringDate(date), Toast.LENGTH_LONG)
                        .show();
                Log.d("car_add_date", getStringDate(date));
            }
        });
        dialog.show();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
            TransitionsUtil.TranslateOfActivity(this, HomeNew.class, Configs.TRANSLATE_STYLE_MAIN_BACK);
//        translate(Configs.TRANSLATE_BACK);
        return super.onKeyDown(keyCode, event);
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
        if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 0 && Math.abs(velocityX) < 20)
            TransitionsUtil.TranslateOfActivity(this, HomeNew.class, Configs.TRANSLATE_STYLE_MAIN_BACK);
        return false;
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(this, Car.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        } else if (Configs.TRANSLATE_CAR_ADD_MAP == position) {

            Intent i = null;
            if (null == i) {
                i = new Intent(this, MapCarAdd.class);
            } else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            if (status.equals(routeStartStr)) {
                i.putExtra(CarAddConfig.TRANSMIT_MAP, CarAddConfig.TRANSMIT_ROUTE_START);
            } else if (status.equals(routeEndStr)) {
                i.putExtra(CarAddConfig.TRANSMIT_MAP, CarAddConfig.TRANSMIT_ROUTE_END);
            }
            Log.d("CarAdd", "status======" + status);
            startActivity(i);
            overridePendingTransition(R.anim.ts_main_in, R.anim.no_main);
        }
    }

    public void getMyShare() {
        if (CarAddConfig.TRANSMIT_ROUTE_START == (int) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_ROUTE_START_STATUS, Configs.TYPE_INTEGER)) {
            routeStartStr = (String) MySharedPreferencesUtils.get(this, CarAddConfig.PLACE_START_MAP, Configs.TYPE_STRING);
            routeStart.setText(routeStartStr);
            float latStart = (float) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_LAT_START, Configs.TYPE_FLOAT);
            float lngStart = (float) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_LNG_START, Configs.TYPE_FLOAT);
            placeStart = new LatLng(latStart, lngStart);
            Log.d("CarAdd", "route_datas=====" + routeStart.getText().toString() + " " + placeStart);
        }
        if (CarAddConfig.TRANSMIT_ROUTE_END == (int) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_ROUTE_END_STATUS, Configs.TYPE_INTEGER)) {
            routeEndStr = (String) MySharedPreferencesUtils.get(this, CarAddConfig.PLACE_END_MAP, Configs.TYPE_STRING);
            routeEnd.setText(routeEndStr);
            float latEnd = (float) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_LAT_END, Configs.TYPE_FLOAT);
            float lngEnd = (float) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_LNG_END, Configs.TYPE_FLOAT);
            placeEnd = new LatLng(latEnd, lngEnd);
            Log.d("CarAdd", "route_datas=====" + routeEnd.getText().toString() + " " + placeEnd);
        }
        if (CarAddConfig.TRANSMIT_TIME_START == (int) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_TIME_START_STATUS, Configs.TYPE_INTEGER)) {
            timeStartStr = (String) MySharedPreferencesUtils.get(this, CarAddConfig.TIME_START_MAP, Configs.TYPE_STRING);
            timeStart.setText(timeStartStr);
            Log.d("CarAdd", "route_datas=====" + timeStart.getText().toString());
        }
        if (CarAddConfig.TRANSMIT_TIME_END == (int) MySharedPreferencesUtils.get(this, CarAddConfig.TRANSMIT_TIME_END_STARTS, Configs.TYPE_INTEGER)) {
            timeEndStr = (String) MySharedPreferencesUtils.get(this, CarAddConfig.TIME_END_MAP, Configs.TYPE_STRING);
            timeEnd.setText(timeEndStr);
            Log.d("CarAdd", "route_datas=====" + timeEnd.getText().toString());
        }
    }

    private int getTime() {
        Calendar calendar = Calendar.getInstance();
        Format format = new SimpleDateFormat("HH:mm");
        long time = 0;
        try {
            Date dateStart = (Date) format.parseObject(timeStartStr);
            Date dateEnd = (Date) format.parseObject(timeEndStr);
            time = dateEnd.getTime() - dateStart.getTime() / 1000;
            Log.d("CarAdd", "time======" + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.round(time);
    }

    private int getDistance() {
        BigDecimal bigDecimal = new BigDecimal(AMapUtils.calculateLineDistance(placeStart, placeEnd));
        int distance = (int) Math.round(Double.parseDouble(bigDecimal.toPlainString()));
        Log.d("CarAdd", "distance======" + distance + " carkind_id" + carkind_id);
        return distance;
    }

    private void getExhaustDetails(int time, int distance, int carkind_id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Configs.URL_HEAD + Configs.URL_EXHAUST_OF_CACULATE + "time=" + time + "&distance=" + distance + "&carkind_id=" + carkind_id;
        Log.d("CarAdd", "url======" + url);
        Request request = new Request.Builder().url(url).build();
        final Gson gson = new Gson();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String res = response.body().string();
                Log.d("CarAdd", "res======" + res);
                GExhasutDetailsList exhasutDetailsList = gson.fromJson(res, GExhasutDetailsList.class);
                Message message = new Message();
                message.what = exhasutDetailsList.status;
                message.obj = exhasutDetailsList.exhaust_details;
                Log.d("CarAdd", "status======" + exhasutDetailsList.status);
                Log.d("CarAdd", "exhaust_details======" + exhasutDetailsList.exhaust_details);
                handler.sendMessage(message);
            }
        });
    }


    private void uploadCarDatas(GExhaustDetails exhaustDetails) {
        Format format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        String urlHead;
        int user_id = (int) MySharedPreferencesUtils.get(this, UserConfig.USER_ID, Configs.TYPE_INTEGER);
        String url = "user_id=" + user_id + "&date=" + date + "&time_begin=" + timeStartStr + "&time_end=" +
                timeEndStr + "&route=" + routeStartStr + "-" + routeEndStr + "&road=" + "0" + "&exhaust=" + exhaustDetails.getExhaust() + "&voc=" + exhaustDetails.getVoc() +
                "&co=" + exhaustDetails.getCo() + "&pm=" + exhaustDetails.getPm() + "&nox=" + exhaustDetails.getNox() + "&gasoline=" + exhaustDetails.getGasoline() +
                "&carkind_id=" + carkind_id + "&drive=" + drive_status;
        if (!"0".equals(MySharedPreferencesUtils.get(this, CarConfig.TRANSMIT_CAR_ID, Configs.TYPE_STRING))) {
            String id = (String) MySharedPreferencesUtils.get(this, CarConfig.TRANSMIT_CAR_ID, Configs.TYPE_STRING);
            urlHead = Configs.URL_HEAD + Configs.URL_UPDATE_CAR_DATAS;
            url = urlHead + url + "&id=" + id;
        } else {
            urlHead = Configs.URL_HEAD + Configs.URL_PUT_CAR_DATAS;
            url = urlHead + url;
        }
        Log.d("CarAdd", "url_upload_car_datas======" + url);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Gson gson = new Gson();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("expected code" + response);
                String res = response.body().string();
                GetStatus getStatus = gson.fromJson(res, GetStatus.class);
                Message message = new Message();
                message.what = getStatus.status;
                handler1.sendMessage(message);
            }
        });
    }

    private class GExhasutDetailsList {
        GExhaustDetails exhaust_details;
        int status;
    }

    private class GetStatus {
        int status;
    }
}
