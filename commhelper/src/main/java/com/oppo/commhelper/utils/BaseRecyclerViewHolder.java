package com.oppo.commhelper.utils;

import android.annotation.Nullable;
import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created on 2018/1/2
 * function : recyclerView 的视图存储器
 *
 * @author LinkTech
 */

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public Activity context;
    private OnViewClickListener listener;

    /**
     * 额外的,可存储的数据
     */
    @Nullable
    private Integer tag;

    public BaseRecyclerViewHolder(View itemView) {
        this(itemView, null);
    }

    public BaseRecyclerViewHolder(View itemView, OnViewClickListener listener) {
        super(itemView);
        context = (Activity) itemView.getContext();
        this.listener = listener;
        if (listener != null) {
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public final void onClick(View v) {
        if (listener != null && getCurrentPosition() >= 0) {
            listener.onViewClick(v, tag != null ? tag : getCurrentPosition());
        }
    }

    /**
     * 获取当前位置
     */
    public int getCurrentPosition() {
        return getAdapterPosition();
    }

    /**
     * 设置监听器,默认在主动设置非空监听的情况下,才会有监听事件
     */
    protected void setOnViewClickListener(OnViewClickListener listener) {
        if (listener != null) {
            this.listener = listener;
            itemView.setOnClickListener(this);
        }
    }

    public <T extends Integer> BaseRecyclerViewHolder setTag(@Nullable T tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 自定义view点击事件
     */
    public interface OnViewClickListener {
        /**
         * @param v        被点击的view
         * @param position 所在的position
         */
        void onViewClick(View v, int position);
    }
}
