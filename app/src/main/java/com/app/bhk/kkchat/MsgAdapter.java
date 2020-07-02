package com.app.bhk.kkchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by thinkpad on 2018/7/13.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>
{
    public List<Msg> msgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftlayout;
        LinearLayout rightlayout;
        TextView leftMsg;
        TextView rightMsg;
        de.hdodenhof.circleimageview.CircleImageView ic;
        public ViewHolder (View v){
            super(v);
            leftlayout=(LinearLayout) v.findViewById(R.id.left_layout);
            rightlayout=(LinearLayout) v.findViewById(R.id.right_layout);
            leftMsg=(TextView) v.findViewById(R.id.left_msg);
            rightMsg=(TextView) v.findViewById(R.id.right_msg);
            ic=(de.hdodenhof.circleimageview.CircleImageView) v.findViewById(R.id.icon_image);
        }
    }
    public MsgAdapter(List<Msg> msgList){
        this.msgList=msgList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
    return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Msg msg=msgList.get(position);
        if(msg.getType()==Msg.TYPE_RECEIVED){
            //如果收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftlayout.setVisibility(View.VISIBLE);
            holder.rightlayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType()==Msg.TYPE_SENT){
            //如果是发出消息，则显示右边的布局，将左边的消息布局yi'ni
            holder.rightlayout.setVisibility(View.VISIBLE);
            holder.leftlayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }
    }
    @Override
    public int getItemCount(){
        return msgList.size();
    }
}
