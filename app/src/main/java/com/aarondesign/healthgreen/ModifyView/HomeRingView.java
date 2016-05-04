package com.aarondesign.healthgreen.ModifyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.HomeNewConfig;

/**
 * Created by Aaron on 2016/4/13 0013.
 */
public class HomeRingView extends View {

    private static final int PERSON_STATUS = 0, CAR_STATUS = 1;
    private int mWidth, mHeight;
    private float xCenter, yCenter, mBigRadius, mSmallRadius, mData = 0;
    private Paint mBigCirclePaint;
    private Paint mSmallCirclePaint;
    private Paint mExhaustPaint;    //  exhaust的数值
    private Paint mTextPaint1;      //  存留体内
    private Paint mTextPaint2;      //  mg

    public HomeRingView(Context context) {
        super(context);
    }

    public HomeRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBigCirclePaint = new Paint();
        mSmallCirclePaint = new Paint();
        mExhaustPaint = new Paint();
        mTextPaint1 = new Paint();
        mTextPaint2 = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mWidth = mHeight;
        xCenter = mWidth / 2;
        yCenter = mHeight / 2;
        mBigRadius = xCenter;
        mSmallRadius = yCenter;
        setMeasuredDimension(mWidth, mHeight);
    }


    private void setPaint(int color) {
        Log.d("HomeRingView", "===ringview是否开硬件加速===" + isHardwareAccelerated());
        setLayerType(LAYER_TYPE_SOFTWARE, null);      // 禁止硬件加速 使用后在Scrollview里会不断重绘
        Log.d("HomeRingView", "===ringview是否开硬件加速===" + isHardwareAccelerated());
        mBigCirclePaint.setColor(color);
        mBigCirclePaint.setStyle(Paint.Style.STROKE);
        mBigCirclePaint.setAntiAlias(true);
        mBigCirclePaint.setStrokeWidth(30);
        mBigCirclePaint.setShadowLayer(10, 0, 1, color);

        mSmallCirclePaint.setColor(getResources().getColor(R.color.color_background_center));
        mSmallCirclePaint.setStyle(Paint.Style.FILL);
        mSmallCirclePaint.setAntiAlias(true);

        int textColor = getResources().getColor(R.color.color_text_white);
        mTextPaint1.setColor(textColor);
        mTextPaint1.setAntiAlias(true);
        mTextPaint1.setStyle(Paint.Style.FILL);
        mTextPaint1.setTextSize(40);
        mTextPaint1.setTextAlign(Paint.Align.CENTER);

        mTextPaint2.setColor(textColor);
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setStyle(Paint.Style.FILL);
        mTextPaint2.setTextSize(30);
        mTextPaint2.setTextAlign(Paint.Align.CENTER);

        mExhaustPaint.setColor(getResources().getColor(R.color.color_blue_1));
        mExhaustPaint.setAntiAlias(true);
        mExhaustPaint.setStyle(Paint.Style.FILL);
        mExhaustPaint.setTextSize(128);
        mExhaustPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(xCenter, yCenter, mBigRadius - 45, mBigCirclePaint);
        canvas.drawText(mData + "", xCenter, yCenter, mExhaustPaint);
        canvas.drawText("存留体内", xCenter, yCenter + 150, mTextPaint1);
        canvas.drawText("mg", xCenter + 135, yCenter + 85, mTextPaint2);
    }

    public void setData(float data,int status) {
        this.mData = data;
        if (HomeNewConfig.HOME_PERSON_STATUS == status) {
            if (data < 20)
                setPaint(getResources().getColor(R.color.color_blue_1));
            else if (data <40 && data>=20)
                setPaint(getResources().getColor(R.color.color_blue_2));
            else if (data < 60 && data>=40)
                setPaint(getResources().getColor(R.color.color_blue_3));
            else if (data < 80 && data>=60)
                setPaint(getResources().getColor(R.color.color_blue_4));
            else
                setPaint(getResources().getColor(R.color.color_blue_5));
        } else if (HomeNewConfig.HOME_CAR_STATUS == status) {
            if (data < 20)
                setPaint(getResources().getColor(R.color.color_blue_1));
            else if (data <40 && data>=20)
                setPaint(getResources().getColor(R.color.color_blue_2));
            else if (data < 60 && data>=40)
                setPaint(getResources().getColor(R.color.color_blue_3));
            else if (data < 80 && data>=60)
                setPaint(getResources().getColor(R.color.color_blue_4));
            else
                setPaint(getResources().getColor(R.color.color_blue_5));
        }
        invalidate();
    }
}
