package com.aarondesign.healthgreen.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;

import com.aarondesign.healthgreen.R;

/**
 * Created by Aaron on 2016/3/23 0023.
 */
public class TimeCountUtil extends CountDownTimer {

    private Context context;
    private Button btn;//按钮

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    public TimeCountUtil(Context mContext, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.context = mContext;
        this.btn = btn;
    }


    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText((millisUntilFinished / 1000) + "s后可重新获取");//设置倒计时时间

        //设置按钮为灰色，这时是不能点击的
        btn.setBackground(context.getResources().getDrawable(R.drawable.time_down_btn_bg));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为白色
        btn.setText(span);

    }


    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("重新获取验证码");
        btn.setClickable(true);//重新获得点击
        btn.setBackground(context.getResources().getDrawable(R.drawable.edittext_bg));//还原背景色

    }


}