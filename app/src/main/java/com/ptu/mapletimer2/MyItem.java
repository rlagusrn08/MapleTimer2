package com.ptu.mapletimer2;

import android.graphics.drawable.Drawable;

public class MyItem {
    private Drawable icon;
    private String name;
    private String time;

    public Drawable getIcon(){
        return icon;
    }
    public void setIcon(Drawable icon){
        this.icon = icon;
    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
}
