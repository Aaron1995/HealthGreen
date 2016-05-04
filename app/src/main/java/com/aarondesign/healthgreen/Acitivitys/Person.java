package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Adapters.GPersonAdapter;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GBean.GPerson;
import com.aarondesign.healthgreen.GreenDaoBackEnd.PersonRepository;
import com.aarondesign.healthgreen.Interface.OnRefreshListener;
import com.aarondesign.healthgreen.ModifyView.PersonPolylineView;
import com.aarondesign.healthgreen.ModifyView.RefreshListView;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.PersonConfig;
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
public class Person extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener, OnRefreshListener {

    private PersonPolylineView personPolylineView;
    private HorizontalScrollView hsv;
    public GPersonAdapter gPersonAdapter;
    private String url = "";
    private ImageView personIVBack;
    private TextView user;
    private GestureDetector detector;
    private Intent translateBack;
    private OkHttpClient okHttpClient;
    public RefreshListView listView;
    public List<GPerson> mGPersons;
    public GPersonList personList;
    private Gson gson;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PersonConfig.DATAS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    personList = (GPersonList) msg.obj;
                    Log.d("msg", "status2--" + personList.status);
                    Log.d("msg", "size2--" + personList.datas.size());
                    putIntoAdapter(personList);
                    break;
                case PersonConfig.DATAS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case PersonConfig.DATAS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        SysApplication.getInstance().addActivity(this);

        init();             //初始化
        findView();         //关联控件
        getPersonDatas();        //获取数据
        initEvent();
        setLineChart();
        setPerson();
    }

    private void setPerson() {
        String userStr = (String) MySharedPreferencesUtils.get(this, UserConfig.USER_NAME, Configs.TYPE_STRING);
        user.setText(userStr);
    }

    private void setLineChart() {
        personPolylineView.setDatas(PersonRepository.getAllPersonBeans(this));
        personPolylineView.setPaints(Color.WHITE);
        personPolylineView.setVisibility(View.VISIBLE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                hsv.scrollTo(personPolylineView.getWidth(), 0);
                hsv.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.person_rl);
//        PersonPolylineView view = new PersonPolylineView(this,null);
//        view.setPaints(Color.WHITE);
//        view.setDatas(PersonRepository.getAllPersonBeans(this));
//        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        rl.setLayoutParams(lp);
//        rl.addView(view);
    }

    private void initEvent() {
        personIVBack.setOnClickListener(this);
        listView.setOnRefreshListener(this);
    }

    private void putIntoAdapter(GPersonList personList) {
        List<GPerson> gp = personList.datas;
        Log.d("msg", personList.datas.get(0).toString());
        for (GPerson gPerson : gp) {
            Log.d("orders", gPerson.getId() + "");
            mGPersons.add(gPerson);
        }

//        if (null == gPersonAdapter) {
        Log.d("msg", "personBridgeList长度为：" + mGPersons.size());
        gPersonAdapter = new GPersonAdapter(Person.this, mGPersons);
//        }
        listView.setAdapter(gPersonAdapter);
    }

    public void init() {
        if (null == detector)
            detector = new GestureDetector(this, this);
        if (null == gson)
            gson = new Gson();

    }

    public void findView() {
        personIVBack = (ImageView) findViewById(R.id.person_iv_back);
        listView = (RefreshListView) findViewById(R.id.person_lv_datas);
        user = (TextView) findViewById(R.id.person_tv_person);
        hsv = (HorizontalScrollView) findViewById(R.id.person_hrs_polyline);
        personPolylineView = (PersonPolylineView) findViewById(R.id.person_line_chart);
    }


    private void getPersonDatas() {
        okHttpClient = new OkHttpClient();
        mGPersons = new ArrayList<>();
        String user_id = String.valueOf(MySharedPreferencesUtils.get(this, UserConfig.USER_ID, Configs.TYPE_INTEGER));
        url = Configs.URL_HEAD + Configs.URL_PERSON_DATAS_OF_USER + user_id;

        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String res = response.body().string();
                GPersonList gPersonList = gson.fromJson(res, GPersonList.class);
                Log.d("msg", "status1--" + gPersonList.status);
                Log.d("msg", "size1--" + gPersonList.datas.size());
                Message message = new Message();
                message.what = gPersonList.status;
                message.obj = gPersonList;
                handler.sendMessage(message);
                Log.d("msg", "返回的body：" + res);
            }
        });
    }

    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                getPersonDatas();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                gPersonAdapter.notifyDataSetChanged();
                listView.hideHeaderView();
//                adapter.notifyDataSetChanged();
//                控制脚布局隐藏
//                rListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void onLoadingMore() {

    }

    private static class GPersonList {
        List<GPerson> datas;
        int status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_iv_back:
                translate(Configs.TRANSLATE_BACK);
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
        if (e1.getX() - e2.getX() > 20 && Math.abs(velocityX) > 0)
            translate(Configs.TRANSLATE_BACK);
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
            overridePendingTransition(R.anim.ts_horizontal3, R.anim.ts_horizontal4);
        }
    }

}
