package com.app.bhk.kkchat;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class mainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private RecyclerView msgRecyclerview,onlineRecyclerview;
    private MsgAdapter adapter;
    private OnlineAdapter online_adapter;
    private String text1;
    private List<Msg> msgList=new ArrayList<>();
    private List<String> online_list=new ArrayList<>();
    int bhk=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMsg();


        msgRecyclerview=(RecyclerView)  findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerview.setLayoutManager(layoutManager);
        adapter=new MsgAdapter(msgList);
        msgRecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(this);
        onlineRecyclerview=(RecyclerView) findViewById(R.id.online_recyclerview);
        onlineRecyclerview.setLayoutManager(layoutManager1);
        online_adapter=new OnlineAdapter(online_list);
        onlineRecyclerview.setAdapter(online_adapter);

        Button button=(Button) findViewById(R.id.input_text);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText editText=(EditText)findViewById(R.id.text);
                String text=editText.getText().toString();//获取发送消息
                //显示本机发出消息。
                if(!"".equals(text)){
                    Msg msg=new Msg(text,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);//插入新消息。
                    msgRecyclerview.scrollToPosition(msgList.size()-1);//定位到最后一行
                    editText.setText("");
                    TcpUtil.sendMessage(text+"\n");
                }

            }
        });
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
        }

        //接受消息的线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                       try {
                           InputStream in = TcpUtil.s.getInputStream();
                           InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                           final BufferedReader br = new BufferedReader(isr);
                           String message = null;


                           String array[];
                           while ((message = br.readLine()) != null) {
                               array=message.split(",");
                               switch (array[0]){
                                   case "0":
                                       text1=array[1]+"上线了";
                                       online_list.add(array[1]);
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {

                                               online_adapter.notifyDataSetChanged();

                                           }
                                       });
                                       break;
                                   case "1":
                                       text1=array[1]+"下线了";
                                       online_list.remove(array[1]);
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {

                                            online_adapter.notifyDataSetChanged();

                                           }
                                       });
                                       break;
                                   case "3":
                                       text1=array[1];
                                       break;
                                   case "4":
                                       text1=null;
                                       online_list.add(array[1]);
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {

                                               online_adapter.notifyDataSetChanged();
                                           }
                                       });
                                       break;
                                   default:
                                       text1=" ";
                               }
                               if(text1!=null) {
                                   Message message1 = new Message();
                                   message1.what = 1;
                                   handler.sendMessage(message1);
                               }

                           }
                       }catch (IOException e){
                           e.printStackTrace();
                       }

            }
        }).start();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Msg msg1=new Msg(text1,Msg.TYPE_RECEIVED);
                    msgList.add(msg1);
                    adapter.notifyItemInserted(msgList.size()-1);//插入新消息。
                    msgRecyclerview.scrollToPosition(msgList.size()-1);//定位到最后一行。*
                    break;
            }
        }
    };

    private  void initMsg(){
        Msg msg=new Msg("欢迎来到KKchat，天涯海角，此刻共你千里婵娟----KK",Msg.TYPE_RECEIVED);
        msgList.add(msg);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TcpUtil.sendMessage("CLOSE_KKchat_@");
        Log.d("KKchat close","test!!!");
        TcpUtil.destroy();
    }
}

