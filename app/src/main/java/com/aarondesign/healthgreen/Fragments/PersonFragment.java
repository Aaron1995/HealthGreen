package com.aarondesign.healthgreen.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aarondesign.healthgreen.Acitivitys.Home;
import com.aarondesign.healthgreen.Adapters.GPersonAdapter;
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
 * Created by Aaron on 2016/4/13 0013.
 */
public class PersonFragment extends Fragment implements View.OnClickListener, OnRefreshListener {

    private PersonPolylineView personPolylineView;
    private HorizontalScrollView hsv;
    public GPersonAdapter gPersonAdapter;
    private String url = "";
    private OkHttpClient okHttpClient;
    public RefreshListView listView;
    public List<GPerson> mGPersons;
    public GPersonList personList;
    private Gson gson;
    private View view;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person,null);
        init();             //初始化
        findView();         //关联控件
        getPersonDatas();        //获取数据
        initEvent();
        setLineChart();
        return view;
    }


    private void setLineChart() {
        personPolylineView.setDatas(PersonRepository.getAllPersonBeans(getActivity()));
        personPolylineView.setPaints(getResources().getColor(R.color.color_blue_2));
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
        gPersonAdapter = new GPersonAdapter(getActivity(), mGPersons);
//        }
        listView.setAdapter(gPersonAdapter);
    }

    public void init() {
        if (null == gson)
            gson = new Gson();

    }

    public void findView() {
        listView = (RefreshListView) view.findViewById(R.id.person_lv_datas);
        hsv = (HorizontalScrollView) view.findViewById(R.id.person_hrs_polyline);
        personPolylineView = (PersonPolylineView) view.findViewById(R.id.person_line_chart);
    }


    private void getPersonDatas() {
        okHttpClient = new OkHttpClient();
        mGPersons = new ArrayList<>();
        String user_id = String.valueOf(MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_ID, Configs.TYPE_INTEGER));
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

        }
    }


}