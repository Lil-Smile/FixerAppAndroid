package com.example.FixerAppAndroid;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Smile on 03.07.15.
 */
public class DataForDB {
    private  int time;
    private String  date;
    private int count;
    private String exercise;


    DataForDB(int t, int c, String e)
    {
        this.time = t;
        this.count = c;
        this.exercise = e;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.date = sdf.format(new Date(System.currentTimeMillis()));
    }


    public int getTime() {
        return time;
    }



    public String getDate() {
        return date;
    }


    public int getCount() {
        return count;
    }

    public String getExercise() {
        return exercise;
    }
}
