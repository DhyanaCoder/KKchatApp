package com.app.bhk.kkchat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class TcpUtil {
    public static  Socket s;
    public static  BufferedWriter bw;
    public static void destroy(){
        try {
            bw.close();
            s.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void sendMessage(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bw.write(text);
                    bw.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
