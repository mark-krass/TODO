package com.example.gambm.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoxAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private ArrayList<UserInfo> list;
    private Context ctx;

    BoxAdapter(Context context, ArrayList<UserInfo> userInfo) {
        ctx = context;
        list = userInfo;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        UserInfo u = getUserInfo(position);
        ((TextView) view.findViewById(R.id.tvGoali)).setText(u.goal);
        return view;
    }

    private UserInfo getUserInfo(int position) {
        return ((UserInfo) getItem(position));
    }

}