package com.eqled.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eqled.eqledcontrol.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    public interface OnItemClickLitener    //设置监听
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    public  interface  OnItemLongClickLitener{ //内部类
        void   OnItemLongClick(View view, int position);

    }
    private OnItemLongClickLitener mOnItemLongClickLitener;
    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener)
    {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    private LayoutInflater mInflater;
    private List<String> mDatas;
    public  RecyclerViewAdapter(Context context, List<String> datats){
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_la,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt =(TextView)view.findViewById(R.id.recyclerview_text);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
         holder.mTxt.setText(mDatas.get(position));
        if (mOnItemClickLitener != null)
        {
            holder.mTxt.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.mTxt, position);
                }
            });

        }
        if(mOnItemLongClickLitener!=null){
            holder.mTxt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickLitener.OnItemLongClick(holder.mTxt,position);

                    return false;
                }
            });




        }




    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
        TextView mTxt;
    }


}
