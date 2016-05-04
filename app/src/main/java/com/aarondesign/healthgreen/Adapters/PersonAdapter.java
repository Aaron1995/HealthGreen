package com.aarondesign.healthgreen.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aarondesign.healthgreen.Bridge.PersonBridge;
import com.aarondesign.healthgreen.R;

import java.util.List;

/**
 * Created by Aaron on 2015/11/9 0009.
 */
public class PersonAdapter extends BaseAdapter {

    private Context context;
    private List<PersonBridge> personBridgeList;
    private LayoutInflater inflater;

    public PersonAdapter(Context context, List<PersonBridge> personBridgeList) {
        this.context = context;
        this.personBridgeList = personBridgeList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return personBridgeList.size();
    }

    @Override
    public Object getItem(int position) {
        return personBridgeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PersonBridge personBridge = personBridgeList.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_person, null);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.person_tv_month);
            holder.tvDay = (TextView) convertView.findViewById(R.id.person_tv_day);
            holder.tvTime = (TextView) convertView.findViewById(R.id.person_tv_time);
            holder.tvRoute = (TextView) convertView.findViewById(R.id.person_tv_route);
            holder.tvFreed = (TextView) convertView.findViewById(R.id.person_tv_freed);
            holder.tvExhaust = (TextView) convertView.findViewById(R.id.person_tv_inhale);
            holder.tvStay = (TextView) convertView.findViewById(R.id.person_tv_stay);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String timeBegin = personBridge.getTimeBegin();
        String timeEnd = personBridge.getTimeEnd();

        holder.tvMonth.setText(personBridge.getMonth());
        holder.tvDay.setText(personBridge.getDay());
        holder.tvTime.setText(timeBegin + " - " + timeEnd);
        holder.tvRoute.setText(personBridge.getPlaceBegin() + " - " + personBridge.getPlaceEnd());
        holder.tvFreed.setText(personBridge.getPersonFreed());
        holder.tvExhaust.setText(personBridge.getPersonExhaust());
        holder.tvStay.setText(personBridge.getPersonStay());
        return convertView;
    }

    class ViewHolder {
        Bitmap btLocation;
        TextView tvMonth;
        TextView tvDay;
        TextView tvTime;
        TextView tvRoute;
        TextView tvFreed;
        TextView tvExhaust;
        TextView tvStay;
    }
}
