package com.android.sexspider4.site.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.sexspider4.R;
import com.android.sexspider4.site.bean.SiteBean;
import com.android.sexspider4.site.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {
    private List<SiteBean> lists;
    private OnItemClickListener listener;
    private Context context;

    public SiteAdapter(List<SiteBean> lists, Context context, OnItemClickListener listener) {
        this.lists = lists;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_site_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(lists.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SiteBean site = lists.get(position);

        holder.title.setText(site.getSiteTitleAndDomain());

        if (site.isUpdated == 1) {
            holder.isUpdated.setText(context.getString(R.string.txt_site_isupdated));
        } else if (site.isUpdated == 2) {
            holder.isUpdated.setText(context.getString(R.string.txt_site_noupdated));
        } else if (site.isUpdated == 3) {
            holder.isUpdated.setText(context.getString(R.string.txt_site_hasupdated));
        } else if (site.isUpdated == 4) {
            holder.isUpdated.setText(context.getString(R.string.txt_site_newupdated));
        } else if (site.isUpdated == 5) {
            holder.isUpdated.setText(context.getString(R.string.txt_site_loading));
        } else {
            holder.isUpdated.setText("");
        }

        if (site.vipLevel > 1) {
            holder.title.setTextColor(context.getResources().getColor(R.color.site_notvip_color));
            holder.isUpdated.setTextColor(context.getResources().getColor(R.color.site_notvip_color));
        } else {
            holder.title.setTextColor(context.getResources().getColor(R.color.site_vip_color));
            holder.isUpdated.setTextColor(context.getResources().getColor(R.color.site_vip_color));
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView isUpdated;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.site_list_title);
            isUpdated = (TextView) itemView.findViewById(R.id.site_list_isupdated);
        }
    }

}
