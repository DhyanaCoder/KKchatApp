package com.app.bhk.kkchat;

/**
 * Created by thinkpad on 2018/7/13.
 */

public class Msg {
public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private String content;
    private int type;
    private int image;
    public Msg(String content, int type){
        this.content=content;
        this.type=type;
    }
    public String getContent(){
        return content;
    }
    public int getType(){
        return  type;
    }
    public int getImage(){
        return image;
    }
    public void setImage(int image){
        this.image=image;
    }
}
