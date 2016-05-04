package com.aarondesign.healthgreen.ModifyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aarondesign.healthgreen.R;
import com.healthwalk.bean.CarBean;
import com.healthwalk.bean.PersonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2016/3/29 0029.
 */
public class CarPolylineView extends View {

    private int width, height;
    private float maxValue;     //传入数据的最大值
    private int dataNum;        //数据总数
    private List<CarBean> datas;
    private Paint mPaintLine;               // 用于画折线
    private Paint mPaintCircle;             // 用于画空心远
    private Paint mPaintTextValue;          // 用于画数据
    private Paint mPaintTextDate;           // 用于画日期
    private Paint mPaintFilledCircle;       // 用于画实心圆
    private Path path;

    public CarPolylineView(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        setWillNotDraw(false);
    }

    public CarPolylineView(Context context, AttributeSet attr) {
        super(context, attr);
        datas = new ArrayList<>();
        mPaintLine = new Paint();
        mPaintCircle = new Paint();
        mPaintTextValue = new Paint();
        mPaintTextDate = new Paint();
        mPaintFilledCircle = new Paint();
        path = new Path();
        setWillNotDraw(false);
    }

    public void setPaints(int color) {
        mPaintLine.setColor(color);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(4);
        mPaintLine.setAntiAlias(true);

        mPaintCircle.setColor(getResources().getColor(R.color.color_bg_white));
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setAntiAlias(true);        // 边缘清晰
        mPaintCircle.setStrokeWidth(5);

        mPaintFilledCircle.setColor(color);
        mPaintFilledCircle.setStyle(Paint.Style.FILL);
        mPaintFilledCircle.setAntiAlias(true);

        mPaintTextValue.setColor(color);
        mPaintTextValue.setTextSize(20);
        mPaintTextValue.setTextAlign(Paint.Align.CENTER);
        mPaintTextValue.setAntiAlias(true);

        mPaintTextDate.setColor(color);
        mPaintTextDate.setAntiAlias(true);
        mPaintTextDate.setTextAlign(Paint.Align.CENTER);
        mPaintTextDate.setTextSize(20);
        //  重绘
        invalidate();
//        forceLayout();
//        requestLayout();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != datas) {
            width = (datas.size() - 1) * xAddedNum + chartMarginHorizontal * 2;
            getMaxValue(datas);
        }
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        Log.d("PersonPolylineView", "===width,height===" + width + "," + height);
    }

    /**
     * 用于得到总数据中最大数据
     *
     * @param datas 总数据
     */
    private void getMaxValue(List<CarBean> datas) {
        maxValue = 0;
        dataNum = 0;
        for (CarBean person : datas) {
            if (Float.parseFloat(person.getExhaust()) > maxValue) {
                maxValue = Float.parseFloat(person.getExhaust());
            }
            dataNum++;
        }
    }


    private int mChartHeight;//折线图的高
    private int mChartWidth;//折线图的宽
    private int startX;//开始绘制的x坐标
    private int startY = 30;//开始绘制的y坐标
    private int chartMarginBottom = 30;//折线图距离父控件底部距离
    private int chartMarginHorizontal = 150;//折线图距离父控件左右的距离
    private int valueAlignLeft = 0;//value参数文本距离左边距离
    private int dateAlignLeft = 0;//date参数文本距离左边距离
    private int valueAlignBottom = 10;//value参数文本距离底部距离
    private int dateAlignBottom = 10;//date参数文本距离底部距离
    private int xAddedNum = 150;//绘制折线图时每次移动的x轴距离
    private int yAddedNum;//绘制折线图时每次移动的y轴距离
    private boolean isDrawFirst = true;//是否是第一次绘制
    private float circleFilledRadius = 10;//外圆半径
    private float circleRadius = 7;//内圆半径

    private float firstX;//第一个点的x轴坐标
    private float firstY;//第一个点的y轴坐标

    public void setDatas(List<CarBean> datas) {
        this.datas = datas;
        Log.d("PersonPolylineView", "===datas.size()===" + datas.size());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != datas) {
            mChartHeight = height - chartMarginBottom;
            yAddedNum = mChartHeight / 4;
            mChartWidth = width - chartMarginHorizontal * 2;
            startX = width - 150;
            // 画折线
            for (CarBean car : datas) {
                float value = Float.parseFloat(car.getExhaust());
                if (isDrawFirst) {
                    //当第一次绘制时得到第一个点的横纵坐标
                    firstX = startX;
                    firstY = startY + (1f - value / ((int) maxValue * 1.5f)) * mChartHeight;
                    path.moveTo(firstX, firstY);
//                    path.setLastPoint(firstX, firstY);
                    isDrawFirst = false;
                }
                //每循环一次，将path线性相位一次
                path.lineTo(startX, startY + (1f - value / ((int) maxValue * 1.5f)) * mChartHeight);
                startX -= xAddedNum;
            }
            //重新给startX赋值
            startX = width - 150;
            canvas.drawPath(path, mPaintLine);
            //画出折线

//        //画出折线以下部分的颜色
//        path.lineTo(startX + mChartWidth, startY + mChartHeight);
//        path.lineTo(startX, startY + mChartHeight);
//        path.lineTo(firstX, firstY);
//        canvas.drawPath(path, mPaintCoveredBg);

            //画出每个点的圆圈，和对应的文本
            for (CarBean car : datas) {
                String date = car.getDate();
                String[] d = date.split("-");
                String day = d[2];
                float value = Float.parseFloat(car.getExhaust());
                canvas.drawCircle(startX, startY + (1f - value / ((int) maxValue * 1.5f)) * mChartHeight, circleFilledRadius, mPaintFilledCircle);
                canvas.drawCircle(startX, startY + (1f - value / ((int) maxValue * 1.5f)) * mChartHeight, circleRadius, mPaintCircle);
                canvas.drawText(day + "", startX + dateAlignLeft, height - dateAlignBottom, mPaintTextDate);
                canvas.drawText(value + "", startX + valueAlignLeft, startY + (1f - value / ((int) maxValue * 1.5f)) * mChartHeight - valueAlignBottom, mPaintTextValue);
                startX -= xAddedNum;
            }
        }
        //在次使startX回到初始值
        startX = width - 150;
        Log.d("PersonPolylineView", "===draw finished===");
        path.reset();
        isDrawFirst = true;
    }

}
