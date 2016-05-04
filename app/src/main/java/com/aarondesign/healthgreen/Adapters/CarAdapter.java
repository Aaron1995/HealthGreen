package com.aarondesign.healthgreen.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Bridge.CarBridge;
import com.aarondesign.healthgreen.R;

import java.util.List;

/**
 * Created by Aaron on 2015/11/17 0017.
 */
public class CarAdapter extends BaseAdapter {

    private Context context;
    private List<CarBridge> carBridgeList;
    private LayoutInflater inflater;

    public CarAdapter(Context context, List<CarBridge> carBridgeList) {
        this.context = context;
        this.carBridgeList = carBridgeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return carBridgeList.size();
    }

    @Override
    public Object getItem(int position) {
        return carBridgeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        CarBridge carBridge = carBridgeList.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_car, null);
            holder.carKind = (ImageView) convertView.findViewById(R.id.car_iv_kind);
            holder.exhaustTrend = (ImageView) convertView.findViewById(R.id.car_iv_exhaust_trend);
            holder.gasolineTrend = (ImageView) convertView.findViewById(R.id.car_iv_gasoline_trend);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.car_tv_day);
            holder.tvDay = (TextView) convertView.findViewById(R.id.car_tv_day);
            holder.tvTime = (TextView) convertView.findViewById(R.id.car_tv_time);
            holder.tvRoute = (TextView) convertView.findViewById(R.id.car_tv_route);
            holder.tvExhauts = (TextView) convertView.findViewById(R.id.car_tv_exhaust);
            holder.tvGasoline = (TextView) convertView.findViewById(R.id.car_tv_gasoline);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String timeBegin = carBridge.getTimeBegin();
        String timeEnd = carBridge.getTimeEnd();

        holder.exhaustTrend.setImageBitmap(carBridge.getBtCarKind());
        holder.exhaustTrend.setImageBitmap(carBridge.getExhaustTrend());
        holder.gasolineTrend.setImageBitmap(carBridge.getGasolineTrend());

        holder.tvMonth.setText(carBridge.getMonth());
        holder.tvDay.setText(carBridge.getDay());
        holder.tvTime.setText(timeBegin + " - " + timeEnd);
        holder.tvRoute.setText(carBridge.getPlaceBegin() + " - " + carBridge.getPlaceEnd());
        holder.tvExhauts.setText(carBridge.getCarExhaust());
        holder.tvGasoline.setText(carBridge.getCarGasoline());

        return convertView;
    }

    class ViewHolder {
        ImageView carKind;
        ImageView exhaustTrend;
        ImageView gasolineTrend;
        TextView tvMonth;
        TextView tvDay;
        TextView tvTime;
        TextView tvRoute;
        TextView tvExhauts;
        TextView tvGasoline;
    }
}
