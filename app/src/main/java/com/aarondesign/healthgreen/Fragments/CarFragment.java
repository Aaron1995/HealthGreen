package com.aarondesign.healthgreen.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Acitivitys.CarAdd;
import com.aarondesign.healthgreen.Acitivitys.Home;
import com.aarondesign.healthgreen.Adapters.GCarAdapter;
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
 * Created by Aaron on 2016/4/13 0013.
 */
public class CarFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, OnRefreshListener {

    private static int TRANSLATE_ADD = 10;
    private static int TRANSLATE_MODIFY = 20;
    private View view;

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
        mCarAdapter = new GCarAdapter(getActivity(), mCars);
//        }
        listView.setAdapter(mCarAdapter);
    }

    public ImageView carIVAdd;

    public Intent translateBack;
    public Intent translateAdd;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car,null);
        init();
        findView();
        initEvent();
        getCarDatas();
        initMyShare();
        setLineChart();
        return view;
    }

    private void setLineChart() {
        carPolylineView.setPaints(getResources().getColor(R.color.color_blue_light));
        carPolylineView.setDatas(CarRepository.getAllCarBeans(getActivity()));
    }

    private void initMyShare() {
        MySharedPreferencesUtils.save(getActivity(), CarConfig.TRANSMIT_CAR_ID, "0");
        MySharedPreferencesUtils.save(getActivity(), CarAddConfig.TRANSMIT_ROUTE_START_STATUS, CarAddConfig.TRANSMIT_ROUTE_FAILURE);
        MySharedPreferencesUtils.save(getActivity(), CarAddConfig.TRANSMIT_ROUTE_END_STATUS, CarAddConfig.TRANSMIT_ROUTE_FAILURE);
        MySharedPreferencesUtils.save(getActivity(), CarAddConfig.TRANSMIT_TIME_START_STATUS, CarAddConfig.TRANSMIT_TIME_FAILURE);
        MySharedPreferencesUtils.save(getActivity(), CarAddConfig.TRANSMIT_TIME_END_STARTS, CarAddConfig.TRANSMIT_TIME_FAILURE);
    }

    private void getCarDatas() {
        mCars = new ArrayList<>();
        String user_id = String.valueOf(MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_ID, Configs.TYPE_INTEGER));
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
//        detector = new GestureDetector(getActivity(), this);
    }

    private void findView() {
        listView = (RefreshListView) view.findViewById(R.id.car_lv_datas);
        carIVAdd = (ImageView) view.findViewById(R.id.car_iv_add);
        hsv = (HorizontalScrollView) view.findViewById(R.id.car_hsv_polyline);
        carPolylineView = (CarPolylineView) view.findViewById(R.id.car_line_chart);
    }

    private void initEvent() {
        carIVAdd.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_iv_add:
                translate(TRANSLATE_ADD);
                break;
        }
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(getActivity(), Home.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            getActivity().overridePendingTransition(R.anim.ts_horizontal1, R.anim.ts_horizontal2);
        } else if (TRANSLATE_ADD == position) {
            if (null == translateAdd)
                translateAdd = new Intent(getActivity(), CarAdd.class);
            else {
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            MySharedPreferencesUtils.save(getActivity(), CarConfig.TRANSLATE_CAR_ADD, CarConfig.TRANSLATE_ADD);
            startActivity(translateAdd);
            getActivity().overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        } else if (TRANSLATE_MODIFY == position) {
            if (null == translateAdd)
                translateAdd = new Intent(getActivity(), CarAdd.class);
            else {
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateAdd.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            MySharedPreferencesUtils.save(getActivity(), CarConfig.TRANSMIT_CAR_ID, carId);
            MySharedPreferencesUtils.save(getActivity(), CarConfig.TRANSLATE_CAR_ADD, CarConfig.TRANSLATE_MODIFY);
            startActivity(translateAdd);
            getActivity().overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            if (null == translateAdd)
                translateAdd = new Intent(getActivity(), CarAdd.class);
            TextView tv = (TextView) view.findViewById(R.id.car_tv_id);
            carId = tv.getText().toString();
            Log.d("Car", "carId======" + carId);
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            translate(TRANSLATE_MODIFY);
        }
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
