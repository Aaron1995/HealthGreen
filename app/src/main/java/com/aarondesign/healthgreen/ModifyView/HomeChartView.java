package com.aarondesign.healthgreen.ModifyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.HomeNewConfig;
import com.healthwalk.bean.CarBean;
import com.healthwalk.bean.PersonBean;

import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Aaron on 2016/4/14 0014.
 */
public class HomeChartView extends View {

    private int status = -1;
    private int view_width, view_height;
    private static final int LINE_WIDTH = 30;
    private static final int CIRCLE_RADIUS = 15;
    private static final int TEXT_DAY_SIZE = 25, TEXT_MONTH_SIZE = 25, TEXT_YEAR_SIZE = 35;
    private int text_value_size = 35;
    private Paint mLinePaint;
    private Paint mCirClePaint;
    private Paint mTextMonthPaint;
    private Paint mTextDayPaint;
    private Paint mTextYearPaint;
    private Paint mTextValuePaint;
    private List<PersonBean> mPersonBeansDatas;
    private List<CarBean> mCarBeanDatas;
    private int datasSizes = 0;

    private float startX = 150, startY = 15, endX = 0, endY = 0;
    private int xAddNum = 250, yAddedNum;
    private int mChartHeight, mChartWidth;
    private int chartMarginBottom = 80, chartMarginHorizontal = 400;
    private int maxDataSubscript = 0;
    private double maxData = 0;

    private void setPaint(int color) {
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(LINE_WIDTH);
        mLinePaint.setAntiAlias(true);
//        mLinePaint.setShadowLayer(5, 0, 0, color);

        mCirClePaint.setColor(color);
        mCirClePaint.setStyle(Paint.Style.FILL);
//        mCirClePaint.setShadowLayer(5, 0, 0, color);
        mCirClePaint.setAntiAlias(true);

        int textColor = getResources().getColor(R.color.color_text_gray);
        mTextMonthPaint.setColor(textColor);
        mTextMonthPaint.setAntiAlias(true);
        mTextMonthPaint.setStyle(Paint.Style.FILL);
        mTextMonthPaint.setTextAlign(Paint.Align.CENTER);
        mTextMonthPaint.setTextSize(TEXT_MONTH_SIZE);

        mTextDayPaint.setColor(textColor);
        mTextDayPaint.setAntiAlias(true);
        mTextDayPaint.setStyle(Paint.Style.FILL);
        mTextDayPaint.setTextAlign(Paint.Align.CENTER);
        mTextDayPaint.setTextSize(TEXT_DAY_SIZE);

        mTextYearPaint.setColor(textColor);
        mTextYearPaint.setAntiAlias(true);
        mTextYearPaint.setStyle(Paint.Style.FILL);
        mTextYearPaint.setTextAlign(Paint.Align.CENTER);
        mTextYearPaint.setTextSize(TEXT_YEAR_SIZE);

        mTextValuePaint.setColor(getResources().getColor(R.color.color_text_white));
        mTextValuePaint.setAntiAlias(true);
        mTextValuePaint.setStyle(Paint.Style.FILL);
        mTextValuePaint.setTextAlign(Paint.Align.CENTER);
        mTextValuePaint.setTextSize(text_value_size);
    }

    private boolean isToday(String day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (day.equals(calendar.get(Calendar.DAY_OF_MONTH)))
            return true;
        else
            return false;
    }

    public void setStatus(int status, Object o) {
        this.status = status;
        if (HomeNewConfig.HOME_PERSON_STATUS == status) {
            // 得出 datasSizes,maxData,maxDataSubscript
            mPersonBeansDatas = (List<PersonBean>) o;
            datasSizes = mPersonBeansDatas.size();
            for (PersonBean personBean : mPersonBeansDatas) {
                if (Double.parseDouble(personBean.getStay()) > maxData) {
                    maxData = Double.parseDouble(personBean.getStay());
                }
                maxDataSubscript++;
            }
        } else if (HomeNewConfig.HOME_CAR_STATUS == status) {
            // 得出 datasSizes,maxData,maxDataSubscript
            mCarBeanDatas = (List<CarBean>) o;
            datasSizes = mCarBeanDatas.size();
            for (CarBean carBean : mCarBeanDatas) {
                if (Double.parseDouble(carBean.getExhaust()) > maxData) {
                    maxData = Double.parseDouble(carBean.getExhaust());
                }
                maxDataSubscript++;
            }
        }
        Log.d("HomeChartView", "===maxData,datasSizes===" + maxData+","+datasSizes);
        setDimension();
        invalidate();
        postInvalidate();
    }

    public HomeChartView(Context context) {
        super(context);
    }

    public HomeChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLinePaint = new Paint();
        mCirClePaint = new Paint();
        mTextMonthPaint = new Paint();
        mTextDayPaint = new Paint();
        mTextYearPaint = new Paint();
        mTextValuePaint = new Paint();
        mPersonBeansDatas = new ArrayList<>();
        mCarBeanDatas = new ArrayList<>();
        setPaint(getResources().getColor(R.color.color_blue_1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (0 != datasSizes) {
            view_width = (datasSizes - 1) * xAddNum + 2 * chartMarginHorizontal;
            view_height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            startY = view_height - 80;
            startX = chartMarginHorizontal;
            setMeasuredDimension(view_width, view_height);
            Log.d("HomeChartView", "===view_width,view_height===" + view_width + "," + view_height);
        }
    }

    private void setDimension() {
        view_width = (datasSizes - 1) * xAddNum + 2 * chartMarginHorizontal;
        Log.d("HomeChartView","===view_width==="+view_width);
        setMeasuredDimension(view_width, view_height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (0 != datasSizes) {
            mChartHeight = view_height - chartMarginBottom;
            if (HomeNewConfig.HOME_PERSON_STATUS == status) {
                String day = "", month = "", year = "";
                boolean isLoop = true;
                for (PersonBean personBean : mPersonBeansDatas) {
                    float value = Float.parseFloat(personBean.getStay());
                    String[] date = String.valueOf(personBean.getDate()).split("-");
                    String day1 = date[2];
                    String month1 = date[1];
                    String year1 = date[0];

                    endX = startX;
                    endY = (float) (1 - value / maxData) * mChartHeight + chartMarginBottom;
                    int differenceY = (int) (startY - endY);
                    if (differenceY < 0) {
                        endY = startY;
                    }
                    if (isToday(day)) {
                        mLinePaint.setColor(getResources().getColor(R.color.color_blue_2));
                        mCirClePaint.setColor(getResources().getColor(R.color.color_blue_2));
                    } else {
                        mLinePaint.setColor(getResources().getColor(R.color.color_blue_1));
                        mCirClePaint.setColor(getResources().getColor(R.color.color_blue_1));
                    }
                    if (isLoop) {
                        isLoop = false;
                        canvas.drawText(month1 + "月", startX - (xAddNum / 2), startY + 55, mTextMonthPaint);
                        canvas.drawText(year1 + "年", startX - (xAddNum / 2), view_height / 2, mTextYearPaint);
                    } else {
                        if (!month1.equals(month))
                            canvas.drawText(month1 + "月", startX - (xAddNum / 2), startY + 55, mTextMonthPaint);
                        if (!year1.equals(year))
                            canvas.drawText(year1 + "年", startX - (xAddNum / 2), view_height / 2, mTextYearPaint);
                    }
                    canvas.drawLine(startX, startY, endX, endY, mLinePaint);
                    canvas.drawCircle(startX, startY, CIRCLE_RADIUS, mCirClePaint);
                    canvas.drawCircle(endX, endY, CIRCLE_RADIUS, mCirClePaint);
                    canvas.drawText(day1, startX, startY + 55, mTextDayPaint);
                    canvas.drawText(value + "", startX, endY - 35, mTextValuePaint);
                    startX += xAddNum;
                    day = day1;
                    month = month1;
                    year = year1;
                }
                startY = view_height - 80;
                startX = chartMarginHorizontal;
            } else if (HomeNewConfig.HOME_CAR_STATUS == status) {
                String day = "", month = "", year = "";
                boolean isLoop = true;
                for (CarBean carBean : mCarBeanDatas) {
                    float value = Float.parseFloat(carBean.getExhaust());
                    String[] date = String.valueOf(carBean.getDate()).split("-");
                    String day1 = date[2];
                    String month1 = date[1];
                    String year1 = date[0];
                    endX = startX;
                    endY = (float) (1 - value / maxData) * mChartHeight + chartMarginBottom;
                    int differenceY = (int) (startY - endY);
                    if (differenceY < 0) {
                        endY = startY;
                    }
                    if (isToday(day)) {
                        mLinePaint.setColor(getResources().getColor(R.color.color_blue_2));
                        mCirClePaint.setColor(getResources().getColor(R.color.color_blue_2));
                    } else {
                        mLinePaint.setColor(getResources().getColor(R.color.color_blue_1));
                        mCirClePaint.setColor(getResources().getColor(R.color.color_blue_1));
                    }
                    if (isLoop) {
                        isLoop = false;
                        canvas.drawText(month1 + "月", startX - (xAddNum / 2), startY + 55, mTextMonthPaint);
                        canvas.drawText(year1 + "年", startX - (xAddNum / 2), view_height / 2, mTextYearPaint);
                    } else {
                        if (!month1.equals(month))
                            canvas.drawText(month1 + "月", startX - (xAddNum / 2), startY + 55, mTextMonthPaint);
                        if (!year1.equals(year))
                            canvas.drawText(year1 + "年", startX - (xAddNum / 2), view_height / 2, mTextYearPaint);
                    }
                    canvas.drawLine(startX, startY, endX, endY, mLinePaint);
                    canvas.drawCircle(startX, startY, CIRCLE_RADIUS, mCirClePaint);
                    canvas.drawCircle(endX, endY, CIRCLE_RADIUS, mCirClePaint);
                    canvas.drawText(day1 + "日", startX, startY + 55, mTextDayPaint);
                    canvas.drawText(value + "", startX, endY - 35, mTextValuePaint);
                    startX += xAddNum;
                    day = day1;
                    month = month1;
                    year = year1;
                }
                startY = view_height - 80;
                startX = chartMarginHorizontal;
            }
            //画圆柱
            maxData = 0;
        }

    }
}
