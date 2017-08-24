package com.fxlweb.mydemo.sites.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlweb.mydemo.R;
import com.fxlweb.mydemo.sites.bean.SitesBean;

import java.util.List;

/**
 * Created by fengxl on 2014/11/30.
 */
public class SitesAdapter extends BaseAdapter {
    private List<SitesBean> lists;
    private LayoutInflater layout;

    public SitesAdapter(Context context, List<SitesBean> lists) {
        this.lists = lists;
        this.layout = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.lists.size();
    }

    @Override
    public SitesBean getItem(int position) {
        return this.lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = layout.inflate(R.layout.list_siteview, parent, false);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtIsUpdated = (TextView) convertView.findViewById(R.id.txt_isupdated);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.i("TAG", "position: " + position);

        SitesBean entity = getItem(position);
        holder.txtTitle.setText(entity.siteRank + ". " + entity.siteName);

        if(entity.isUpdated == 1) {
            holder.txtIsUpdated.setVisibility(View.VISIBLE);
        } else {
            holder.txtIsUpdated.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void updateData(List<SitesBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private TextView txtTitle;
        private TextView txtIsUpdated;
    }
}
