package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GBean.GCarKind;
import com.aarondesign.healthgreen.GBean.GUser;
import com.aarondesign.healthgreen.GreenDaoBackEnd.CarRepository;
import com.aarondesign.healthgreen.GreenDaoBackEnd.PersonRepository;
import com.aarondesign.healthgreen.ModifyView.ClearEditText;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarKindConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.PersonConfig;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.TransitionsUtil;
import com.google.gson.Gson;
import com.healthwalk.bean.CarBean;
import com.healthwalk.bean.PersonBean;
import com.healthwalk.dao.DaoMaster;
import com.healthwalk.dao.DaoSession;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Login extends Activity implements View.OnClickListener {

    private Button loginYes;
    private Button registerYes;
    private Intent translateRegister;
    private Intent translateHome;
    private LinearLayout ancestor;

    private OkHttpClient okHttpClient;
    private ClearEditText user;
    private ClearEditText pwd;
    private Gson gson;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UserConfig.CHECK_FAILURE:
                    Toast.makeText(Login.this, "帐号或者密码错误!", Toast.LENGTH_SHORT).show();
                    break;

                case UserConfig.LOGIN_SUCCESS:
                    Toast.makeText(Login.this, "登陆成功!", Toast.LENGTH_SHORT).show();
                    saveGreenDao();
//                    long startTime = System.nanoTime();
//                    if (null == translateHome)
//                        translateHome = new Intent(Login.this, HomeNew.class);
//                    else {
//                        translateHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        translateHome.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    }
//                    startActivity(translateHome);
//                    overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
//                    long time = System.nanoTime() - startTime;
//                    Log.d("msg", "时间为：" + time);
                    TransitionsUtil.TranslateOfActivity(Login.this,HomeNew.class,Configs.TRANSLATE_STYLE_MAIN);
                    break;
            }
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PersonConfig.DATAS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    MPersonList personList = (MPersonList) msg.obj;
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

    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PersonConfig.DATAS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    MCarList carList= (MCarList) msg.obj;
                    Log.d("msg", "status2--" + carList.status);
                    Log.d("msg", "size2--" + carList.datas.size());
                    putIntoAdapter(carList);
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

    private void putIntoAdapter(Object object) {
        if(object instanceof  MPersonList) {
            MPersonList personList = (MPersonList) object;
            List<PersonBean> gp = personList.datas;
            Log.d("msg", personList.datas.get(0).toString());
            PersonRepository.clearPerson(this);
            for (PersonBean gPerson : gp) {
                Log.d("orders", gPerson.toString());
//            mGPersons.add(gPerson);
                PersonRepository.insertOrUpdate(this, gPerson);
            }
        }else if(object instanceof  MCarList){
            MCarList carList = (MCarList) object;
            List<CarBean> gp = carList.datas;
            CarRepository.clearCar(this);
            for (CarBean carBean : gp){
                CarRepository.insertOrUpdate(this,carBean);
            }
        }
    }

    private void saveGreenDao() {
        String url = Configs.URL_HEAD + Configs.URL_PERSON_DATAS_OF_USER + MySharedPreferencesUtils.get(this, UserConfig.USER_ID, Configs.TYPE_INTEGER);
        Log.d("Login",url);
        OkHttpClient okHttpClient = new OkHttpClient();
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
                MPersonList mPersonList = gson.fromJson(res, MPersonList.class);
                Log.d("msg", "status1--" + mPersonList.status);
                Log.d("msg", "size1--" + mPersonList.datas.size());
                Message message = new Message();
                message.what = mPersonList.status;
                message.obj = mPersonList;
                handler.sendMessage(message);
                Log.d("msg", "返回的body：" + res);
            }
        });

        String url1 = Configs.URL_HEAD +Configs.URL_CAR_DATAS_OF_USER +MySharedPreferencesUtils.get(this,UserConfig.USER_ID,Configs.TYPE_INTEGER);
        OkHttpClient okHttpClient1 = new OkHttpClient();
        Request request1 = new Request.Builder().url(url1).build();
        final Gson gson1 = new Gson();
        okHttpClient1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())throw new IOException("Unexpected code"+response);
                String res = response.body().string();
                MCarList mCarList = gson1.fromJson(res, MCarList.class);
                Message message = new Message();
                message.what = mCarList.status;
                message.obj = mCarList;
                handler1.sendMessage(message);
            }
        });
    }

    private class MPersonList {
        List<PersonBean> datas;
        int status;
    }

    private class MCarList {
        List<CarBean> datas;
        int status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SysApplication.getInstance().addActivity(this);

        findView();         // 关联控件
        init();             //
        initEvent();        // 创建监听
    }

    private void init() {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
    }

    private void initEvent() {
        loginYes.setOnClickListener(this);
        registerYes.setOnClickListener(this);
        ancestor.setOnClickListener(this);
    }

    private void findView() {
        user = (ClearEditText) findViewById(R.id.login_user);
        pwd = (ClearEditText) findViewById(R.id.login_pwd);
        loginYes = (Button) findViewById(R.id.login_yes);
        registerYes = (Button) findViewById(R.id.iv_register);
        ancestor = (LinearLayout) findViewById(R.id.login_ancestor);
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
                        }
                    }).create();
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_yes:
                if (user.getText().length() != 0 && pwd.getText().length() != 0){
                    login();
                }
                else if (user.getText().length() != 0)
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                else if (pwd.getText().length() != 0)
                    Toast.makeText(this, "用户不能为空！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "用户名与密码不能为空！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_register:
                if (null == translateRegister) {
                    translateRegister = new Intent(this, Register.class);
                }
                startActivity(translateRegister);
                overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
                break;
            case R.id.login_ancestor:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
        }
    }

    private void login() {
        String userStr = user.getText().toString().trim();
        String pwdStr = pwd.getText().toString();
        String get = Configs.URL_HEAD + Configs.URL_LOGIN_USER + "phone=" + userStr + "&password=" + pwdStr;
//        String get = loginUrl + "?phone=" + userStr + "&password=" + pwdStr;

        Request request = new Request.Builder().url(get).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Toast.makeText(Login.this,"请检查您的网络！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                String example = response.toString();
                String res = response.body().string();
                Log.d("msg", "返回的body：" + res);
                getStatus(res);
            }
        });
    }

    private void getStatus(String json) {
        try {
            JSONObject result = new JSONObject(json);
            int status = result.getInt("status");
            Message message = new Message();
            if (status == UserConfig.LOGIN_SUCCESS) {
                MySharedPreferencesUtils.save(this, Configs.LOGIN_STATUS, Configs.LOGIN_STATUS);  // 将成功登录的用户记为已登录状态
                MySharedPreferencesUtils.save(this, Configs.LOGIN_DATETIME, System.currentTimeMillis());
                GInfo gInfo = gson.fromJson(json, GInfo.class);
                GUser user = gInfo.user;
                MySharedPreferencesUtils.save(this, UserConfig.USER_ID, user.getId());
                GCarKind gCarKind = gInfo.carkind;
                saveDatasToSP(user, gCarKind);

                message.what = UserConfig.LOGIN_SUCCESS;
                mHandler.sendMessage(message);
            } else if (status == UserConfig.LOGIN_FAILURE) {
                message.what = UserConfig.LOGIN_FAILURE;
                mHandler.sendMessage(message);
            } else if (status == UserConfig.LOGIN_ERROR) {
                message.what = UserConfig.LOGIN_ERROR;
                mHandler.sendMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GInfo {
        GUser user;
        GCarKind carkind;
    }

    public void saveDatasToSP(GUser user, GCarKind carKind) {
        MySharedPreferencesUtils.save(this, UserConfig.USER_ID, user.getId());
        MySharedPreferencesUtils.save(this, UserConfig.USER_PHONE, user.getPhone());
        MySharedPreferencesUtils.save(this, UserConfig.USER_PASSWORD, user.getPassword());
        MySharedPreferencesUtils.save(this, UserConfig.USER_NAME, user.getName());
        MySharedPreferencesUtils.save(this, UserConfig.USER_GENDER, user.getGender());
        MySharedPreferencesUtils.save(this, UserConfig.USER_WEIGHT, user.getWeight());
        MySharedPreferencesUtils.save(this, UserConfig.USER_HEIGHT, user.getHeight());
        MySharedPreferencesUtils.save(this, UserConfig.USER_AGE, user.getAge());
        MySharedPreferencesUtils.save(this, UserConfig.USER_CARKIND_ID, user.getCarkind_id());
        MySharedPreferencesUtils.save(this, UserConfig.USER_FVC, user.getFvc());
        MySharedPreferencesUtils.save(this, UserConfig.USER_CITY, user.getCity());
        MySharedPreferencesUtils.save(this, UserConfig.USER_REGISTER_DATETIME, user.getId());
        MySharedPreferencesUtils.save(this, CarKindConfig.KIND, carKind.getKind());
        MySharedPreferencesUtils.save(this, CarKindConfig.POWER, carKind.getPower());
        MySharedPreferencesUtils.save(this, CarKindConfig.DISPLACEMENT, carKind.getDisplacement());
        MySharedPreferencesUtils.save(this, CarKindConfig.WEIGHT, carKind.getWeight());

        // 保存登录状态
        MySharedPreferencesUtils.save(this, UserConfig.LOGIN_STATUS, UserConfig.LOGIN_SUCCESS);
    }

    private void getClearEditTextStatus(){

    }
}
