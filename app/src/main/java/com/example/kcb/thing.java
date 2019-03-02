package com.example.kcb;

import java.util.Date;

/**
 * Created by liu98 on 2019/3/2
 * Email: lcs1998@vip.qq.com
 */
public class thing {
    private String name;
    private int day;
    private String miaoshu;
    private String thing_icon;
    private String  date;

    public thing(String name, int day, String miaoshu, String thing_icon, String date) {
        this.name = name;
        this.day = day;
        this.miaoshu = miaoshu;
        this.thing_icon = thing_icon;
        this.date =date;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public String getThing_icon() {
        return thing_icon;
    }

    public String getDate(){
        return date;
    }
}
