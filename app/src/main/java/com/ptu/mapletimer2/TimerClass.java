package com.ptu.mapletimer2;

import java.io.Serializable;

public class TimerClass implements Serializable {
    String name;
    Integer icon;
    int time;

    public TimerClass(String name, Integer icon, int time)
    {
        this.name = name;
        this.icon = icon;
        this.time = time;
    }
}
