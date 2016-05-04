package com.aarondesign.healthgreen.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aarondesign.healthgreen.GBean.GPerson;
import com.aarondesign.healthgreen.R;
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
public class GPersonAdapter extends BaseAdapter {

    private Context context;
    private List<GPerson> gPersons;
    private LayoutInflater layoutInflater;

    public GPersonAdapter(Context context, List<GPerson> gPersons) {
        this.context = context;
        this.gPersons = gPersons;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return gPersons.size();
    }

    @Override
    public Object getItem(int position) {
        return gPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GPerson person = gPersons.get(position);
        holder = new ViewHolder();
        convertView = layoutInflater.inflate(R.layout.item_person, null);
        Log.d("GpersonAdapter_details", "position:" + position + " " + convertView);
        holder.tvYear = (TextView) convertView.findViewById(R.id.person_tv_year);
        holder.tvMonth = (TextView) convertView.findViewById(R.id.person_tv_month);
        holder.tvDay = (TextView) convertView.findViewById(R.id.person_tv_day);
        holder.tvTime = (TextView) convertView.findViewById(R.id.person_tv_time);
        holder.tvRoute = (TextView) convertView.findViewById(R.id.person_tv_route);
        holder.tvFreed = (TextView) convertView.findViewById(R.id.person_tv_freed);
        holder.tvInhale = (TextView) convertView.findViewById(R.id.person_tv_inhale);
        holder.tvStay = (TextView) convertView.findViewById(R.id.person_tv_stay);

        String timeBegin = person.getTime_begin();
        String timeEnd = person.getTime_end();
        String[] date = person.getDate().split("-");

        holder.tvMonth.setText(date[1]);
        holder.tvYear.setText(date[0]);
        if (position > 0) {
            String[] date1 = gPersons.get(position - 1).getDate().split("-");
            if (date1[1].equals(date[1]))
                holder.tvMonth.setVisibility(View.INVISIBLE);
            if (date1[0].equals(date[0]))
                holder.tvYear.setVisibility(View.INVISIBLE);
            Log.d("GPersonAdapter","date1[1]======"+date1[1] +" date[1]======"+date[1]);
        }
        holder.tvDay.setText(date[2]);
        holder.tvTime.setText(timeBegin + " - " + timeEnd);
        holder.tvRoute.setText(person.getRoute());
        holder.tvFreed.setText(person.getFree());
        holder.tvInhale.setText(person.getInhale());
        holder.tvStay.setText(person.getStay());

        return convertView;
    }

    class ViewHolder {
        Bitmap btLocation;
        TextView tvYear;
        TextView tvMonth;
        TextView tvDay;
        TextView tvTime;
        TextView tvRoute;
        TextView tvFreed;
        TextView tvInhale;
        TextView tvStay;
    }
}
