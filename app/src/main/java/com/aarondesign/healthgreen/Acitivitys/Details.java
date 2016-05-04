package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.os.Bundle;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.R;

/**
 * Created by Aaron on 2015/11/9 0009.
 */
public class Details extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SysApplication.getInstance().addActivity(this);
    }
}
