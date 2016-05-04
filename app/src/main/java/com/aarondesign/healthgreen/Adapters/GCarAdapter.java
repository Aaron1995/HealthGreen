package com.aarondesign.healthgreen.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarondesign.healthgreen.GBean.GCar;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarConfig;
import com.aarondesign.healthgreen.Util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Aaron on 2015/12/30 0030.
 */
public class GCarAdapter extends BaseAdapter {

    private Context context;
    private List<GCar> gCars;
    private LayoutInflater layoutInflater;

    public GCarAdapter(Context context, List<GCar> gCars) {
        this.context = context;
        this.gCars = gCars;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gCars.size();
    }

    @Override
    public Object getItem(int position) {
        return gCars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GCar car = gCars.get(position);
        holder = new ViewHolder();
        convertView = layoutInflater.inflate(R.layout.item_car, null);
        holder.carKind = (ImageView) convertView.findViewById(R.id.car_iv_kind);
        holder.exhaustTrend = (ImageView) convertView.findViewById(R.id.car_iv_exhaust_trend);
        holder.gasolineTrend = (ImageView) convertView.findViewById(R.id.car_iv_gasoline_trend);
        holder.tvYear = (TextView) convertView.findViewById(R.id.car_tv_year);
        holder.tvMonth = (TextView) convertView.findViewById(R.id.car_tv_month);
        holder.tvDay = (TextView) convertView.findViewById(R.id.car_tv_day);
        holder.tvTime = (TextView) convertView.findViewById(R.id.car_tv_time);
        holder.tvRoute = (TextView) convertView.findViewById(R.id.car_tv_route);
        holder.tvExhauts = (TextView) convertView.findViewById(R.id.car_tv_exhaust);
        holder.tvGasoline = (TextView) convertView.findViewById(R.id.car_tv_gasoline);
        holder.tvCarId = (TextView) convertView.findViewById(R.id.car_tv_id);

        String timeBegin = car.getTime_begin();
        String timeEnd = car.getTime_end();
        String[] date = car.getDate().split("-");
        holder.tvMonth.setText(date[1]);
        holder.tvYear.setText(date[0]);
        if (position > 0) {
            String[] date1 = gCars.get(position - 1).getDate().split("-");
            if (date[1].equals(date1[1]))
                holder.tvMonth.setVisibility(View.INVISIBLE);
            if (date1[0].equals(date[0]))
                holder.tvYear.setVisibility(View.INVISIBLE);
            Log.d("GCarAdapter","date1[1]======"+date1[1] +" date[1]======"+date[1]);
        }
        holder.tvDay.setText(date[2]);
        holder.tvTime.setText(timeBegin + " - " + timeEnd);
        holder.tvRoute.setText(car.getRoute());
        holder.tvExhauts.setText(car.getExhaust());
        holder.tvGasoline.setText(car.getGasoline());
        holder.tvCarId.setText(String.valueOf(car.getId()));
        holder.tvCarId.setVisibility(View.INVISIBLE);
        Bitmap bitmap;
        if (CarConfig.DRIVER_SELF == car.getDrive()) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_rise);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_drop);
        }
        holder.exhaustTrend.setImageBitmap(bitmap);
        holder.exhaustTrend.setImageBitmap(bitmap);
        return convertView;
    }

    class ViewHolder {
        ImageView carKind;
        ImageView exhaustTrend;
        ImageView gasolineTrend;
        TextView tvYear;
        TextView tvMonth;
        TextView tvDay;
        TextView tvTime;
        TextView tvRoute;
        TextView tvExhauts;
        TextView tvGasoline;
        TextView tvCarId;
    }
}
