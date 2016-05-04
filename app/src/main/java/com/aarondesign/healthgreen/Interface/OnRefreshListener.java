package com.aarondesign.healthgreen.Interface;

/**
 * Created by Aaron on 2016/3/10 0010.
 */
public interface OnRefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
