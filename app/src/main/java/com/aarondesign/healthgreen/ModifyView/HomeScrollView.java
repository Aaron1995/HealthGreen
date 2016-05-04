package com.aarondesign.healthgreen.ModifyView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Aaron on 2016/4/11 0011.
 */
public class HomeScrollView extends ScrollView {

    private float xDistance, yDistance, startX, startY;

    public HomeScrollView(Context context) {
        super(context);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                xDistance = yDistance = 0f;
                Log.d("HomeScrollView", "onInterceptHoverEvent===运行action_down");
                break;
            case MotionEvent.ACTION_MOVE:
                float lastX = event.getX();
                float lastY = event.getY();
                xDistance = Math.abs(lastX - startX);
                yDistance = Math.abs(lastY - startY);
                Log.d("HomeScrollView","onInterceptHoverEvent===运行action_move");
                if (xDistance > yDistance) {
                    return false;
                }
                break;
        }
        return super.onInterceptHoverEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }
}
