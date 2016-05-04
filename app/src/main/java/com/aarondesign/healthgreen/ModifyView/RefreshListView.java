package com.aarondesign.healthgreen.ModifyView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aarondesign.healthgreen.Interface.OnRefreshListener;
import com.aarondesign.healthgreen.R;

import java.text.SimpleDateFormat;

/**
 * Created by Aaron on 2016/3/10 0010.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "RefreshListView";
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int downX; // 按下时x轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

    private ImageView ivArrow; // 头布局的剪头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间

    private OnRefreshListener mOnRefershListener;
    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取LayoutInflater实例对象
        initHeaderView();
//        initFooterView();
        this.setOnScrollListener(this);
    }

//    /**
//     * 初始化脚布局
//     */
//    private void initFooterView() {
//        footerView = View.inflate(getContext(), R.layout.item_listview_bottom, null);
//        footerView.measure(0, 0);
//        footerViewHeight = footerView.getMeasuredHeight();
//        footerView.setPadding(0, -footerViewHeight, 0, 0);
//        this.addFooterView(footerView);
//    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {

        headerView = View.inflate(getContext(),R.layout.item_listview_top,null);
//        headerView = View.inflate(getContext(), R.layout.item_listview_top, null);
        ivArrow = (ImageView) headerView
                .findViewById(R.id.iv_listview_header_arrow);
        mProgressBar = (ProgressBar) headerView
                .findViewById(R.id.pb_listview_header);
        tvState = (TextView) headerView
                .findViewById(R.id.tv_listview_header_state);

        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
//        headerView.measure(0,56);
//        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
//        headerViewHeight = headerView.getHeight();
        measureView(headerView);
        headerViewHeight = headerView.getMeasuredHeight();
        Log.d("RefreshListView","===headerViewHeight==="+headerViewHeight);
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        initAnimation();
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 测量视图的大小
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

//        downAnimation = new RotateAnimation(-180f, -360f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        downAnimation.setDuration(500);
//        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                setParentScrollAble(false);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int disX = (int) Math.abs(ev.getX() - downX);
                int disY = (int) Math.abs(ev.getY() - downY);
                int disXY = disX - disY;
                // 移动中的y - 按下的y = 间距.
                if (disXY > 20)
                    setParentScrollAble(true);
                int diff = (moveY - downY) / 2;
                Log.d("RefreshListView", "===diff===" + diff);
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = -headerViewHeight + diff;
                // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                if (firstVisibleItemPosition == 0
                        && -headerViewHeight < paddingTop) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                        Log.i(TAG, "松开刷新");
                        tvState.setText("松开刷新");
                        currentState = RELEASE_REFRESH;
//                        refreshHeaderView();
                    } else if (paddingTop < 0
                            && currentState == RELEASE_REFRESH) { // 没有显示完全
                        Log.i(TAG, "下拉刷新");
                        tvState.setText("下拉刷新");
                        currentState = DOWN_PULL_REFRESH;
//                        refreshHeaderView();
                    }
                    // 下拉头布局
                    headerView.setPadding(0, -headerViewHeight + diff, 0, 0);
                    super.onTouchEvent(ev); //调用父类方法，防止滑动时触发点击事件
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    Log.i(TAG, "刷新数据.");
                    // 把头布局设置为完全显示状态
                    headerView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    refreshHeaderView();

                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    // 隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                setParentScrollAble(true);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    // 设置父控件是否可以获取到触摸处理权限
    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }


    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
//        initHeaderView();
        switch (currentState) {
            case DOWN_PULL_REFRESH: // 下拉刷新状态
                tvState.setText("下拉刷新");
                ivArrow.startAnimation(downAnimation); // 执行向下旋转
                break;
            case RELEASE_REFRESH: // 松开刷新状态
                tvState.setText("松开刷新");
                ivArrow.startAnimation(upAnimation); // 执行向上旋转
                break;
            case REFRESHING: // 正在刷新中状态
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                tvState.setText("正在刷新中...");
                break;
            default:
                break;
        }
    }

    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

//        if (scrollState == SCROLL_STATE_IDLE
//                || scrollState == SCROLL_STATE_FLING) {
//            // 判断当前是否已经到了底部
//            if (isScrollToBottom && !isLoadingMore) {
//                isLoadingMore = true;
//                // 当前到底部
//                Log.i(TAG, "加载更多数据");
//                footerView.setPadding(0, 0, 0, 0);
//                this.setSelection(this.getCount());
//
//                if (mOnRefershListener != null) {
//                    mOnRefershListener.onLoadingMore();
//                }
//            }
//        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem 当前屏幕显示在顶部的item的position
     * @param visibleItemCount 当前屏幕显示了多少个条目的总数
     * @param totalItemCount   ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        ivArrow.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        tvState.setText("下拉刷新");
        currentState = DOWN_PULL_REFRESH;
    }

}