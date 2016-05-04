package com.aarondesign.healthgreen;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.aarondesign.healthgreen.Acitivitys.CarAdd;
import com.aarondesign.healthgreen.Acitivitys.Home;
import com.aarondesign.healthgreen.Acitivitys.HomeNew;
import com.aarondesign.healthgreen.Acitivitys.Login;
import com.aarondesign.healthgreen.Acitivitys.Welcome;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;


public class MainActivity extends AppCompatActivity {
    private int loginStatus = 0;
    private Intent jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);

        int width = this.getResources().getDisplayMetrics().widthPixels;
        int height = this.getResources().getDisplayMetrics().heightPixels;
        MySharedPreferencesUtils.save(this, Configs.SCREEN_WIDTH, width);
        MySharedPreferencesUtils.save(this, Configs.SCREEN_HEIGHT, height);
        loginStatus = (int) MySharedPreferencesUtils.get(this, Configs.LOGIN_STATUS, Configs.TYPE_INTEGER);
//        if (Configs.LOGINED_SUCCESS == loginStatus) {
//            jump = new Intent(this, HomeNew.class);
//            startActivity(jump);
//        } else {
//            jump = new Intent(this, Login.class);
//            startActivity(jump);
//        }
        Log.d("MainActivity", "===启动了onCreate===");
        jump = new Intent(this, Login.class);
        startActivity(jump);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Rect outRect = new Rect();
            this.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
            Log.d("MainActivity", "===outRect.top,outRect.bottom,ouRect.height===" + outRect.top + "," + outRect.bottom + "," + outRect.height());
            this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
            Log.d("MainActivity", "===outRect.height===" + outRect.height());
            int navigation_bar_height = getResources().getDimensionPixelSize(getResources().getIdentifier("navigation_bar_height","dimen", "android"));
            Log.d("MainActivity", "===navigation_bar_height===" + navigation_bar_height);
        }
    }
}
