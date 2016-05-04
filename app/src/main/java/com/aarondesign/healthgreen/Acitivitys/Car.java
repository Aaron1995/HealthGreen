package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Adapters.CarAdapter;
import com.aarondesign.healthgreen.Adapters.GCarAdapter;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.Bridge.CarBridge;
import com.aarondesign.healthgreen.GBean.GCar;
import com.aarondesign.healthgreen.GreenDaoBackEnd.CarRepository;
import com.aarondesign.healthgreen.Interface.OnRefreshListener;
import com.aarondesign.healthgreen.ModifyView.CarPolylineView;
import com.aarondesign.healthgreen.ModifyView.RefreshListView;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarAddConfig;
import com.aarondesign.healthgreen.Static.CarConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Car extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener,
        AdapterView.OnItemClickListener, OnRefreshListener {

    private static int TRANSLATE_ADD = 10;
    private static int TRANSLATE_MODIFY = 20;
    public GestureDetector detector;

    public RefreshListView listView;
    private HorizontalScrollView hsv;
    private CarPolylineView carPolylineView;
    private List<GCar> mCars;
    private GCarList mCarList;
    private GCarAdapter mCarAdapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Gson gson = new Gson();
    private String url = "";
    private String carId = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CarConfig.CAR_DATAS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    mCarList = (GCarList) msg.obj;
                    putIntoAdapter(mCarList);
                    break;
                case CarConfig.CAR_DATAS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case CarConfig.CAR_DATAS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    private void putIntoAdapter(GCarList mCarList) {
        for (GCar car : mCarList.datas) {
            Log.d("msg", "car_datas_id:" + car.getId());
            mCars.add(car);
        }

//        if (null == mCarAdapter) {
        mCarAdapter = new GCarAdapter(Car.this, mCars);
//        }
        listView.setAdapter(mCarAdapter);
    }

    public ImageView carIVHome;
    public ImageView carIVAdd;

    public Intent translateBack;
    public Intent translateAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        SysApplication.getInstance().addActivity(this);

        init();
        findView();
        initEvent();
        getCarDatas();
        initMyShare();
        setLineChart();
    }

    private void setLineChart() {
        carPolylineView.setPaints(getResources().getColor(R.color.color_blue_light));
        carPolylineView.setDatas(CarRepository.getAllCarBeans(this));
    }

    private void initMyShare() {
        MySharedPreferencesUtils.save(this, CarConfig.TRANSMIT_CAR_ID, "0");
        MySharedPreferencesUtils.save(this, CarAddConfig.TRANSMIT_ROUTE_START_STATUS, CarAddConfig.TRANSMIT_ROUTE_FAILURE);
        MySharedPreferencesUtils.save(this, CarAddConfig.TRANSMIT_ROUTE_END_STATUS, CarAddConfig.TRANSMIT_ROUTE_FAILURE);
        MySharedPreferencesUtils.save(this, CarAddConfig.TRANSMIT_TIME_START_STATUS, CarAddConfig.TRANSMIT_TIME_FAILURE);
        MySharedPreferencesUtils.save(this, CarAddConfig.TRANSMIT_TIME_END_STARTS, CarAddConfig.TRANSMIT_TIME_FAILURE);
    }

    private void getCarDatas() {
        mCars = new ArrayList<>();
        String user_id = String.valueOf(MySharedPreferencesUtils.get(this, UserConfig.USER_ID, Configs.TYPE_INTEGER));
        String url = Configs.URL_HEAD + Configs.URL_CAR_DATAS_OF_USER + user_id;

        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String result = response.body().string();
                GCarList carList = gson.fromJson(result, GCarList.class);
                Log.d("msg", "status1--" + carList.status);
                Log.d("msg", "size1--" + carList.datas.size());
                Message message = new Message();
                message.what = carList.status;
                message.obj = carList;
                handler.sendMessage(message);
            }
        });
    }


    private void init() {
        detector = new GestureDetector(this, this);
    }

    private void findView() {
        listView = (RefreshListView) findViewById(R.id.car_lv_datas);
        carIVHome = (ImageView) findViewById(R.id.car_iv_back);
        carIVAdd = (ImageView) findViewById(R.id.car_iv_add);
        hsv = (HorizontalScrollView) findViewById(R.id.car_hsv_polyline);
        carPolylineView = (CarPolylineView) findViewById(R.id.car_line_chart);
    }

    private void initEvent() {
        carIVHome.setOnClickListener(this);
        carIVAdd.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_iv_back:
                translate(Configs.TRANSLATE_BACK);
                break;
            case R.id.car_iv_add:
                translate(TRANSLATE_ADD);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            translate(Configs.TRANSLATE_BACK);
        }

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
        if (e2.getX() - e1.getX() > 20 && Math.abs(velocityX) > 0) {
            translate(Configs.TRANSLATE_BACK);
            Log.d("msg", "已使用onFling方法");
        }
        return false;
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(this, Home.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            overridePendingTransition(R.anim.ts_horizontal1, R.anim.ts_horizontal2);
        } else if (TRANSLATE_ADD == position) {
            if (null == translateAdd)
                translateAdd = new Intent(this, CarAdd.class);
            else {
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            MySharedPreferencesUtils.save(this, CarConfig.TRANSLATE_CAR_ADD, CarConfig.TRANSLATE_ADD);
            startActivity(translateAdd);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        } else if (TRANSLATE_MODIFY == position) {
            if (null == translateAdd)
                translateAdd = new Intent(this, CarAdd.class);
            else {
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            MySharedPreferencesUtils.save(this, CarConfig.TRANSMIT_CAR_ID, carId);
            MySharedPreferencesUtils.save(this, CarConfig.TRANSLATE_CAR_ADD, CarConfig.TRANSLATE_MODIFY);
            startActivity(translateAdd);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null == translateAdd)
            translateAdd = new Intent(Car.this, CarAdd.class);
        TextView tv = (TextView) view.findViewById(R.id.car_tv_id);
        carId = tv.getText().toString();
        Log.d("Car", "carId======" + carId);
        translate(TRANSLATE_MODIFY);
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                getCarDatas();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mCarAdapter.notifyDataSetChanged();
                listView.hideHeaderView();
//                adapter.notifyDataSetChanged();
//
//                // 控制脚布局隐藏
//                rListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {

    }

    private class GCarList {
        List<GCar> datas;
        int status;
    }

}
