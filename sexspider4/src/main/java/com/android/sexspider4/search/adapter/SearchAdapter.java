package com.android.sexspider4.search.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sexspider4.R;
import com.android.sexspider4.search.bean.SearchBean;
import com.android.sexspider4.search.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by feng on 2017/5/5.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchBean> lists;
    private OnItemClickListener listener;

    public SearchAdapter(List<SearchBean> lists, OnItemClickListener listener) {
        this.lists = lists;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_view, parent, false);
        final SearchAdapter.ViewHolder holder = new SearchAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(lists.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
        ImageView deleteView = (ImageView) view.findViewById(R.id.search_list_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(lists.get(holder.getAdapterPosition()).searchName, holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchBean search = lists.get(position);
        holder.title.setText(search.searchName);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.search_list_title);
            imageView = (ImageView) itemView.findViewById(R.id.search_list_delete);
        }
    }

}
