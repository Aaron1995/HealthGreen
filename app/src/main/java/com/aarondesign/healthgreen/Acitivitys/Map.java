package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GBean.GRoadInfo;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.RoadInfoConfig;
import com.aarondesign.healthgreen.Util.TransitionsUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Gradient;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlayOptions;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Map extends Activity implements LocationSource, View.OnClickListener, AMapLocationListener, GestureDetector.OnGestureListener {


    public AMap aMap;
    public MapView mapView;
    public OnLocationChangedListener mListener;
    public LocationManagerProxy mAMapLocationManager;
    public Marker marker;// 定位雷达小图标
    public AMapLocation aMapLocation;// 用于判断定位超时
    private Intent translateBack;
    private GestureDetector detector;
    public float mZoom = 12;//缩放级别
    public float mBearing = 0;//方向
    public float mTilt = 0;//倾斜角度

    public Handler hd1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RoadInfoConfig.ROADINFO_STATUS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    GRoadInfoList mRoadInfoList;
                    mRoadInfoList = (GRoadInfoList) msg.obj;
                    initDataAndHeatMap(mRoadInfoList, G_HEATMAP_STATUS_1);
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    public Handler hd2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RoadInfoConfig.ROADINFO_STATUS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    GRoadInfoList mRoadInfoList;
                    mRoadInfoList = (GRoadInfoList) msg.obj;
                    initDataAndHeatMap(mRoadInfoList, G_HEATMAP_STATUS_1);
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    public Handler hd3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RoadInfoConfig.ROADINFO_STATUS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    GRoadInfoList mRoadInfoList;
                    mRoadInfoList = (GRoadInfoList) msg.obj;
                    initDataAndHeatMap(mRoadInfoList, G_HEATMAP_STATUS_1);
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    public Handler hd4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RoadInfoConfig.ROADINFO_STATUS_SUCCESS:
                    Log.d("msg", "成功获取到服务器数据");
                    GRoadInfoList mRoadInfoList;
                    mRoadInfoList = (GRoadInfoList) msg.obj;
                    initDataAndHeatMap(mRoadInfoList, G_HEATMAP_STATUS_1);
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_FAILURE:
                    Log.d("msg", "获取服务器数据失败");
                    break;
                case RoadInfoConfig.ROADINFO_STATUS_ERROR:
                    Log.d("msg", "获取服务器数据错误");
                    break;
            }
        }
    };

    private void getRoadInfoDatas(int status, final Handler handler) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Gson gson = new Gson();
        String road_id = String.valueOf(status);
        String url = Configs.URL_HEAD + Configs.URL_ROADINFO_OF_STATUS + road_id;

        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String result = response.body().string();
                GRoadInfoList roadInfoList = gson.fromJson(result, GRoadInfoList.class);
                Log.d("msg", "road_net_status======" + roadInfoList.status);
//                Log.d("msg","road_net_size======"+roadInfoList.road_info.size());
                Message message = new Message();
                message.what = roadInfoList.status;
                Log.d("msg", "roadInfoList.road_info.get(0)========" + roadInfoList.road_info.get(0).toString());
                message.obj = roadInfoList;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_map);
        SysApplication.getInstance().addActivity(this);

        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        getRoadInfoDatas(RoadInfoConfig.STATUS_1, hd1);
        getRoadInfoDatas(RoadInfoConfig.STATUS_2, hd2);
        getRoadInfoDatas(RoadInfoConfig.STATUS_3, hd3);
        getRoadInfoDatas(RoadInfoConfig.STATUS_4, hd4);


//        queryRoute();//在地图上画出运动轨迹


    }

    private static final int[] COLOR_HEATMAP_STATUS_1 = {
            Color.argb(0, 179, 176, 189),
            Color.argb(51, 170, 176, 189),
            Color.argb(102, 170, 176, 189),
            Color.argb(153, 170, 176, 189),
            Color.argb(204, 170, 176, 189),
            Color.argb(255, 170, 176, 189)
    };

    private static final int[] COLOR_HEATMAP_STATUS_2 = {
            Color.argb(0, 179, 176, 189),
            Color.argb(51, 145, 153, 169),
            Color.argb(102, 145, 153, 169),
            Color.argb(153, 145, 153, 169),
            Color.argb(204, 145, 153, 169),
            Color.argb(255, 145, 153, 169)
    };

    private static final int[] COLOR_HEATMAP_STATUS_3 = {
            Color.argb(0, 179, 176, 189),
            Color.argb(51, 122, 131, 149),
            Color.argb(102, 122, 131, 149),
            Color.argb(153, 122, 131, 149),
            Color.argb(204, 122, 131, 149),
            Color.argb(255, 122, 131, 149)
    };

    private static final int[] COLOR_HEATMAP_STATUS_4 = {
            Color.argb(0, 179, 176, 189),
            Color.argb(51, 98, 108, 127),
            Color.argb(102, 98, 108, 127),
            Color.argb(153, 98, 108, 127),
            Color.argb(204, 98, 108, 127),
            Color.argb(255, 98, 108, 127)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {0.1f,0.2f, 0.5f, 0.7f, 0.9f, 1f};
//    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {100.0f, 50.3f, 20.5f, 10.7f, 5.0f, 1.0f};

    public static final Gradient G_HEATMAP_STATUS_1 = new Gradient(
            COLOR_HEATMAP_STATUS_1, ALT_HEATMAP_GRADIENT_START_POINTS);
    public static final Gradient G_HEATMAP_STATUS_2 = new Gradient(
            COLOR_HEATMAP_STATUS_2, ALT_HEATMAP_GRADIENT_START_POINTS);
    public static final Gradient G_HEATMAP_STATUS_3 = new Gradient(
            COLOR_HEATMAP_STATUS_3, ALT_HEATMAP_GRADIENT_START_POINTS);
    public static final Gradient G_HEATMAP_STATUS_4 = new Gradient(
            COLOR_HEATMAP_STATUS_4, ALT_HEATMAP_GRADIENT_START_POINTS);


    private void initDataAndHeatMap(GRoadInfoList roadInfoList, Gradient gradient) {

        int rage = roadInfoList.road_info.size();
        LatLng[] latlngs = new LatLng[rage];
        int i = 0;
        for (GRoadInfo roadInfo : roadInfoList.road_info) {
            Log.d("msg", "car_road_info_id:" + roadInfo.getId());
            double latitude = (roadInfo.getStart_x() + roadInfo.getEnd_x()) / 2;
            double longitude = (roadInfo.getStart_y() + roadInfo.getEnd_y()) / 2;
            Log.d("msg", "roadInfo.getStart_x(): " + roadInfo.getStart_x() + "=====latitude======" + latitude + "=====longitude=====" + longitude);
            Log.d("msg", "roadInfo.getEnd_x(): " + roadInfo.getEnd_x());
            Log.d("msg", "roadInfo.toString()=====" + roadInfo.toString());
            latlngs[i] = new LatLng(longitude, latitude);
            Log.d("msg", "latlngs[" + i + "]:" + latlngs[i]);
            i++;
        }

        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(Arrays.asList(latlngs)).gradient(gradient)
                .build();
        aMap.addTileOverlay(new
                        TileOverlayOptions()
                        .tileProvider(heatmapTileProvider)
        );

    }


    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        if (null == detector)
            detector = new GestureDetector(this, this);
        mAMapLocationManager = LocationManagerProxy.getInstance(this);
        /*
         * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
        mAMapLocationManager.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 10, this);
//        handler.postDelayed(this, 12000);// 设置超过12秒还没有定位到就停止定位

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
        giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
        marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icons(giflist).period(500));
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.alpha(0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.alpha(0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        stopLocation();// 停止定位
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }


    /**
     * 此方法已经废弃
     */


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null && aLocation != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
            marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
                    .getLongitude()));// 定位雷达小图标
            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    marker.getPosition(), mZoom, mTilt, mBearing)), null);
            this.aMapLocation = aLocation;// 判断超时机制
            Double geoLat = aLocation.getLatitude();
            Double geoLng = aLocation.getLongitude();
            String cityCode = "";
            String desc = "";
            Bundle locBundle = aLocation.getExtras();
            if (locBundle != null) {
                cityCode = locBundle.getString("citycode");
                desc = locBundle.getString("desc");
            }
            String Location = aLocation.getPoiName();

//            Intent i = new Intent(this, MyService.class);
//            Bundle b = new Bundle();
//            b.putDouble("geoLat", geoLat);
//            b.putDouble("geoLng", geoLng);
//            b.putString("Location", Location);
//            Log.d("msg", "显示坐标与名字:" + geoLat + geoLng + "是否具体地址:" + Location);
//            i.putExtras(i);
//
//            startActivity(i);
        }
    }

//    @Override
//    public void run() {
//        if (aMapLocation == null) {
//            Toast.makeText(this, "定位失败，请重新定位", Toast.LENGTH_LONG).show();
//            stopLocation();// 销毁掉定位
//        }
//    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

//    public void queryRoute() {
//        BmobQuery<CoorRoute> query = new BmobQuery<>();
////查询playerName叫“比目”的数据
//        query.addWhereGreaterThan("Nom", 0);
//        query.order("Nom");
////执行查询方法
//        query.findObjects(getActivity(), new FindListener<CoorRoute>() {
//            @Override
//            public void onSuccess(List<CoorRoute> object) {
//                // TODO Auto-generated method stub
//                Double[][] datas = new Double[object.size()][2];
//                for (int i = 0; i < object.size() - 1; i++) {
//                    datas[i][0] = object.get(i).getXCoor();
//                    datas[i][1] = object.get(i).getYCoor();
//                    datas[i + 1][0] = object.get(i + 1).getXCoor();
//                    datas[i + 1][1] = object.get(i + 1).getYCoor();
//                    if (i <= 3) {
//                        aMap.addPolyline((new PolylineOptions())
//                                .add(new LatLng(datas[i][1], datas[i][0]), new LatLng(datas[i + 1][1], datas[i + 1][0]))
//                                .width(15).setDottedLine(false)
//                                .color(Color.argb(255, 69, 132, 229)));
//                    } else if (i <= 7) {
//                        aMap.addPolyline((new PolylineOptions())
//                                .add(new LatLng(datas[i][1], datas[i][0]), new LatLng(datas[i + 1][1], datas[i + 1][0]))
//                                .width(15).setDottedLine(false)
//                                .color(Color.argb(255, 58, 86, 126)));
//                    } else {
//                        aMap.addPolyline((new PolylineOptions())
//                                .add(new LatLng(datas[i][1], datas[i][0]), new LatLng(datas[i + 1][0], datas[i + 1][1]))
//                                .width(15).setDottedLine(false)
//                                .color(Color.argb(255, 50, 50, 50)));
//                    }
//                }
//
//                Log.d("msg", "成功" + object.size());
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//                // TODO Auto-generated method stub
//                Log.d("msg", "查询失败：" + msg);
//            }
//        });
//    }//画线

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    protected void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
//        调用moveCamera方法
        aMap.moveCamera(update);
//        不调用CancelableCallback方法
//        aMap.animateCamera(update,1000,callback);
    }


    public void drawLine() {
        aMap.addPolyline((new PolylineOptions())
                .add(new LatLng(30.288569, 120.013586), new LatLng(30.288532, 120.013366), new LatLng(30.287522, 120.013769))
                .width(15).setDottedLine(false)
                .color(Color.argb(255, 69, 132, 229)));
    }

    /**
     * 重写返回事件
     *
     * @param keyCode 按键类型
     * @param event   产生的事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            TransitionsUtil.TranslateOfActivity(this,HomeNew.class,Configs.TRANSLATE_STYLE_MAIN_BACK);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 0)
            TransitionsUtil.TranslateOfActivity(this,HomeNew.class,Configs.TRANSLATE_STYLE_MAIN_BACK);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private class GRoadInfoList {
        List<GRoadInfo> road_info;
        int status;
    }
}
