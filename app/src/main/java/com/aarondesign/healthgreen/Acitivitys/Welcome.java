package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.R;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Welcome extends Activity implements View.OnClickListener {

    private TextView textView1;
    private TextView textView2;
    private ImageView imageView1;
    private ImageView imageView2;

    private Animation animation;
    private Intent tsLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SysApplication.getInstance().addActivity(this);

        init();             //初始化
        findView();         //关联控件
        initEvent();        //初始化事件
        viewEvent();        //view事件

    }

    private void initEvent() {
        imageView2.setOnClickListener(this);
    }

    private void viewEvent() {

        animation = AnimationUtils.loadAnimation(this,R.anim.a_welcome);
        textView1.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this,R.anim.a_welcome_1);
        textView2.startAnimation(animation);
        imageView1.startAnimation(animation);
        imageView2.startAnimation(animation);
    }

    private void findView() {
        textView1 = (TextView) findViewById(R.id.welcome_tv1);
        textView2 = (TextView) findViewById(R.id.welcome_tv2);
        imageView1 = (ImageView) findViewById(R.id.welcome_iv1);
        imageView2 = (ImageView) findViewById(R.id.welcome_iv2);
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.welcome_iv2:
                if(null == tsLogin)
                    tsLogin = new Intent(this,Login.class);
                startActivity(tsLogin);
                this.finish();
                overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
                break;
        }
    }

    /**
     * 重写返回事件
     * @param keyCode 按键类型
     * @param event 产生的事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
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
}
