package com.app.bhk.kkchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import okio.Buffer;

public class LoginActivity extends AppCompatActivity {
    private CheckBox mCheckBox;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    EditText account_editText;
    EditText password_editText;
    Socket s0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initUI();

        editor=getSharedPreferences("accountdata",MODE_PRIVATE).edit();
        pref=getSharedPreferences("accountdata",MODE_PRIVATE);
        if(pref.getBoolean("check",false)){
            mCheckBox.setChecked(true);
            account_editText.setText(pref.getString("account","null"));
            password_editText.setText(pref.getString("password","null"));
        }
    }
    private void initUI(){
        //登录按钮
        Button loginButton=(Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSever("175.24.101.56",8780);
            }
        });
        //用户名文本
         account_editText=(EditText) findViewById(R.id.account_editText);
         //密码文本
         password_editText=(EditText) findViewById(R.id.password_edittext);
         //记住密码
        mCheckBox=(CheckBox) findViewById(R.id.remember_password);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                editor.putBoolean("check",isChecked);
            }
        });

    }
    private void loginSever(final String ipAddress,final int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    s0 = new Socket(ipAddress, port);//申请链接
                    OutputStream os = s0.getOutputStream();
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(new OutputStreamWriter( s0.getOutputStream(),"utf-8"));
                    bufferedWriter.write(account_editText.getText()+"\n"+password_editText.getText()+"\n");
                    bufferedWriter.flush();

                    InputStreamReader isr=new InputStreamReader(s0.getInputStream(),"UTF-8");
                    BufferedReader br=new BufferedReader(isr);
                    String msg=br.readLine();

                     //根据回传的代码判断是否登录成功，如果登录失败原因是什么。
                    if(msg.equals("good")){
                        //登录成功
                        Intent intent=new Intent(LoginActivity.this,mainActivity.class);
                        startActivity(intent);
                        TcpUtil.s=s0;
                        TcpUtil.bw=bufferedWriter;
                        editor.putString("account",account_editText.getText().toString());
                        editor.putString("password",password_editText.getText().toString());
                        editor.apply();
                    }else if(msg.equals("bad1")){
                        //密码或者账户错误！
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"密码或者账户错误！",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(msg.equals("bad2")){
                        //账号已登录
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"账号已经登录了，不可同时在两个设备上登录一个账号",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();

        }



}
