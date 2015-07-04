package com.example.FixerAppAndroid;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Smile on 03.07.15.
 */
public class StatActivity extends Activity {


    final String TAG = "LOGGED";
    final String TABLE_NAME="FIXER_TABLE";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_activity);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.TableStatLayout);



        DBHelper dbHelper = new DBHelper(this,"FIXER_DB",null,1); //database
        SQLiteDatabase database = dbHelper.getWritableDatabase();



        Cursor c = database.query(TABLE_NAME,null,null,null,null,null,null,null);

        int i = 0;
        if (c.moveToFirst())
        {
            int idColIndex = c.getColumnIndex("id");
            int timeColIndex = c.getColumnIndex("time");
            int dateColIndex = c.getColumnIndex("date");
            int countColIndex = c.getColumnIndex("points");
            int exerciseColIndex = c.getColumnIndex("exercise");

            TableRow tableRow = (TableRow)findViewById(R.id.tableRowEtalon);
            ViewGroup.LayoutParams lp = tableRow.getChildAt(0).getLayoutParams();

            do {
                int id = c.getInt(idColIndex);
                int time = c.getInt(timeColIndex);
                String date = c.getString(dateColIndex);
                int points = c.getInt(countColIndex);
                String exercise = c.getString(exerciseColIndex);

                Log.d(TAG,id+" "+time+" "+date+" "+points+" "+exercise);

                TableRow tr = new TableRow(this);
                TextView timeView = new TextView(this);
                TextView dateView = new TextView(this);
                TextView pointsView = new TextView(this);
                TextView exerciseView = new TextView(this);

                timeView.setText(Integer.valueOf(time).toString());
                dateView.setText(date);
                pointsView.setText(Integer.valueOf(points).toString());
                exerciseView.setText(exercise);

                timeView.setLayoutParams(lp);
                dateView.setLayoutParams(lp);
                pointsView.setLayoutParams(lp);
                exerciseView.setLayoutParams(lp);

                timeView.setTextSize(15);
                dateView.setTextSize(15);
                pointsView.setTextSize(15);
                exerciseView.setTextSize(15);

                tr.addView(timeView);
                tr.addView(dateView);
                tr.addView(exerciseView);
                tr.addView(pointsView);

                Log.d(TAG,Integer.valueOf(i++).toString());

                tableLayout.addView(tr);
            } while (c.moveToNext());
        }

        database.close();
    }








    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table "+TABLE_NAME+" (id integer primary key autoincrement,"
            + "time integer, "
            + "date text, "
            + "points integer, "
            + "exercise text);" +
                    "");
            Log.d(TAG,"created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}