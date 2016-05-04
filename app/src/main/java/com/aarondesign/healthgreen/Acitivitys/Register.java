package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.ModifyView.ClearEditText;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.TimeCountUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Register extends Activity implements GestureDetector.OnGestureListener,
        View.OnClickListener {
    private static final int PWD_SHOW = 1;
    private static final int PWD_HIDE = 0;
    private static final int CODE_VERTIFY = 1;
    private static final int CODE_INVERTIFY = 0;
    private int code_status = CODE_INVERTIFY;
    private int pwd_status = 0;
    private ImageView ivOnTurn;
    private Button ivUserSet;
    private ImageView ivPwd;
    private ClearEditText cetUser;
    private ClearEditText cetPwd;
    private ClearEditText cetCode;
    private Button btnCode;

    private GestureDetector detector;

    private Intent translateBack;
    private Intent translateSetting;

    private OkHttpClient okHttpClient;
    private Gson gson;
    private String url = "";
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UserConfig.CHECK_SUCCESS:
                    Toast.makeText(Register.this, "手机号已经被注册！", Toast.LENGTH_LONG).show();
                    break;
                case UserConfig.CHECK_FAILURE:
                    if (CODE_INVERTIFY == code_status) {
                        Toast.makeText(Register.this, "验证码不正确！", Toast.LENGTH_SHORT).show();
                    } else {
                        MySharedPreferencesUtils.save(Register.this, UserConfig.USER_PHONE, cetUser.getText().toString());
                        MySharedPreferencesUtils.save(Register.this, UserConfig.USER_PASSWORD, cetPwd.getText().toString());
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        df.format(new Date());
                        Log.d("register_datetime", df.format(new Date()));
                        MySharedPreferencesUtils.save(Register.this, UserConfig.USER_REGISTER_DATETIME, df.format(new Date()));
                        translate(Configs.TRANSLATE_SETTING);
                    }
                    break;
                case UserConfig.CHECK_ERROR:
                    Toast.makeText(Register.this, "发生了一点小问题请重试！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void checkRegister(String userStr) {
        url = Configs.URL_HEAD + Configs.URL_CHECK_USER + userStr;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
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
            if (UserConfig.CHECK_SUCCESS == status) {
                message.what = UserConfig.CHECK_SUCCESS;
                hander.sendMessage(message);
            } else if (UserConfig.CHECK_FAILURE == status) {
                message.what = UserConfig.CHECK_FAILURE;
                hander.sendMessage(message);
            } else if (UserConfig.CHECK_ERROR == status) {
                message.what = UserConfig.CHECK_ERROR;
                hander.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SysApplication.getInstance().addActivity(this);
        detector = new GestureDetector(this, this);

        findView();
        init();
        initEvent();
        vertifyCode(); // Mob短信验证码初始化
    }

    private void vertifyCode() {
        SMSSDK.initSDK(this, Configs.MOB_APP_KEY, Configs.MOB_APP_SECRET);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Log.d("Register", "===提交验证码成功====");
                        code_status = CODE_VERTIFY;
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        Log.d("Register", "===获取验证码成功====");
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    private void findView() {
        cetUser = (ClearEditText) findViewById(R.id.register_cet_user);
        cetPwd = (ClearEditText) findViewById(R.id.register_cet_password);
        cetCode = (ClearEditText) findViewById(R.id.register_cet_code);
        ivPwd = (ImageView) findViewById(R.id.register_iv_pwd);
        btnCode = (Button) findViewById(R.id.register_btn_code);
        ivOnTurn = (ImageView) findViewById(R.id.register_iv_on_turn);
        ivUserSet = (Button) findViewById(R.id.register_iv_user_set);
    }

    private void init() {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
    }

    private void initEvent() {
        cetUser.setInputType(InputType.TYPE_CLASS_NUMBER);
        cetUser.setOnClickListener(this);
        cetPwd.setOnClickListener(this);
        cetCode.setOnClickListener(this);
        ivPwd.setOnClickListener(this);
        btnCode.setOnClickListener(this);
        ivOnTurn.setOnClickListener(this);
        ivUserSet.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
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
        // 将该Activity上的触碰事件交给GestureDetector处理
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 触碰是按下时触发该方法
//        Toast.makeText(this, "OnDown", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 0) {
            translate(Configs.TRANSLATE_BACK);
        }

        // 当用户在屏幕上“拖动”时触发该方法
        Log.d("msg", "触发onFling方法");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // 当用户在屏幕上长按时触发该方法
//        Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // 当屏幕“滚动”时触发该方法
//        Toast.makeText(this, "onScroll", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // 当用户在触摸屏幕上按下、而且还未移动和松开时触发该方法
//        Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // 在屏幕上的轻击事件将会触发该方法
//        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_iv_on_turn:
                translate(Configs.TRANSLATE_BACK);
                break;
            case R.id.register_iv_user_set:
                if (null == cetUser.getText().toString() || "".equals(cetUser.getText().toString())) {
                    Toast.makeText(this, "请输入要注册的手机号码！", Toast.LENGTH_LONG).show();
                } else if (null == cetPwd.getText().toString() || "".equals(cetPwd.getText().toString())) {
                    Toast.makeText(this, "请输入您的登陆密码！", Toast.LENGTH_LONG).show();
                } else if (null == cetCode.getText().toString() || "".equals(cetCode.getText().toString())) {
                    Toast.makeText(this, "请输入您收到的短信验证码！", Toast.LENGTH_LONG).show();
                } else if ("".equals(cetCode.getText().toString().trim()) || cetCode.getText().toString().trim().length() != 4) {
                    Toast.makeText(this, "请输入四位验证码数字！", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode(Configs.MOB_COUNTRY_CHINA,cetUser.getText().toString().trim(),cetCode.getText().toString().trim());
                    checkRegister(cetUser.getText().toString());
                }
                break;
            case R.id.register_iv_pwd:
                if (PWD_SHOW == pwd_status) {
                    pwd_status = PWD_HIDE;
                    ivPwd.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_pwd_hide));
                    cetPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    pwd_status = PWD_SHOW;
                    ivPwd.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_pwd_visible));
                    cetPwd.setInputType(0);
                }
                break;
            case R.id.register_btn_code:
                if ("".equals(cetUser.getText().toString())) {
                    Toast.makeText(this, "请输入要发送验证码的手机号码！", Toast.LENGTH_LONG).show();
                } else if (cetUser.getText().toString().length() == 11) {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(this,60000,1000,btnCode);
                    timeCountUtil.start();
                    SMSSDK.getVerificationCode(Configs.MOB_COUNTRY_CHINA, cetUser.getText().toString());
                    Toast.makeText(this, "已发送验证码到您的手机！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(this, Login.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        } else if (Configs.TRANSLATE_SETTING == position) {
            if (null == translateSetting) {
                translateSetting = new Intent();
                translateSetting.putExtra("状态", Configs.REGISTER_STATUS);
                translateSetting.setClass(this, Settings.class);
            } else {
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateSetting);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }
}