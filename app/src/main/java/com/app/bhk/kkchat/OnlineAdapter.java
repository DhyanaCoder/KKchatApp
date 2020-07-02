package com.app.bhk.kkchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {
    private List<String> online_list;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView OnlineName_TextView;
        public ViewHolder(View v){
            super(v);
            OnlineName_TextView=(TextView)v.findViewById(R.id.online_name);

        }
    }
    public OnlineAdapter(List<String> mList){
        this.online_list=mList;
    }
    @Override
    public OnlineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_item,parent,false);
        return new OnlineAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String online_name=online_list.get(position);
        holder.OnlineName_TextView.setText(online_name);
    }
    @Override
    public int getItemCount(){
        return online_list.size();
    }
}


