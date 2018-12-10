package com.android.sexspider4.list.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sexspider4.R;
import com.android.sexspider4.helper.ImageHelper;
import com.android.sexspider4.list.bean.ListBean;
import com.android.sexspider4.list.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ListBean> lists;
    private OnItemClickListener listener;
    private Context context;
    private List positionList = new ArrayList();

    public ListAdapter(List<ListBean> lists, Context context, OnItemClickListener listener) {
        this.lists = lists;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_list_view, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(lists.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(lists.get(holder.getAdapterPosition()), holder.getAdapterPosition());
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ListBean list = lists.get(position);
        //viewHolder.title.setText(list.siteInfo.siteRank + "." + list.listTitle);
        viewHolder.title.setText(list.listNum + ") " + list.listTitle);//与列表顶部要一至 updateTipsView
        //viewHolder.title.setText(list.listTitle);

        if (list.loadNum > 0) {
            viewHolder.loadNum.setText(String.valueOf(list.loadNum));
        } else {
            viewHolder.loadNum.setText("");
        }

        viewHolder.imageView.setVisibility(View.GONE);
        if (list.listPicture != null && !list.listPicture.equals("")) {
            Bitmap bitmap = ImageHelper.getThumbImage(list);
            if(bitmap != null) {
                viewHolder.imageView.setImageBitmap(bitmap);
                viewHolder.imageView.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.favoriteView.setVisibility(View.GONE);
        if (list.isFavorite == 1) {
            viewHolder.favoriteView.setVisibility(View.VISIBLE);
        }

        //视频标志
        if(list.siteInfo.IsVideo()) {
            viewHolder.title.setText(viewHolder.title.getText() + " " + context.getString(R.string.txt_video));
        }

        if (list.isDowning == 1) {
            if (list.downStatus == 2) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_hasdown));
            } else if (list.downStatus == 3) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_iserror));
            }
        }

        if (list.isDowning == 0) {
            if (list.isNew == 1) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_isnew));
            } else if (list.isDown == 0) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_notdown));
            } else if (list.isRead == 1 && list.isDown == 1) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_isread));
            } else if (list.isRead == 1 && list.isDown != 1) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_hasdown_isread));
            } else if (list.isDown == 2) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_hasdown));
            } else if (list.isDown == 3) {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_iserror));
            } else {
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.list_isdown));
            }
        }

        if (list.isDowning == 1) {
            viewHolder.progressBar.setVisibility(View.VISIBLE);
        } else {
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        ImageView favoriteView;
        ProgressBar progressBar;
        TextView loadNum;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_list_title);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            favoriteView = (ImageView) itemView.findViewById(R.id.favoriteView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            loadNum = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public void setLists(List<ListBean> lists) {
        this.lists = lists;
    }

}
