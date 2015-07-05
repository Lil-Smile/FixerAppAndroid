package com.example.FixerAppAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.FixerAppAndroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smile on 29.06.15.
 */
public class TableActivity extends Activity implements View.OnClickListener {


    final String TAG = "LOGGED";
    final String TABLE_NAME="FIXER_TABLE";
    final int DIALOG = 1;
    final String[] exercisesString = {"Рывок","Толчок","Д.цикл"};
    Integer[] timeInt = new Integer[60];
    long timeOfCurrentExercise=0;
    int globalSeconds=0;

    List<DataForDB> dataForDBs = new ArrayList<DataForDB>(10);
    List<Integer> timeList = new ArrayList<Integer>(10);



    List<TextView> exerciseListTextView = new ArrayList<TextView>(60); //для лэйблов с подходами
    List<TextView> pointListTextView = new ArrayList<TextView>(60); //очки
    List<Button> timeListButton = new ArrayList<Button>(60); //время


    TableLayout tl;
    TableRow tableRow; //строчка уже готовая и ее детки
    TextView textView;
    TextView textView1;
    Button button;
    Button startButton;
    Button statButton;
    Button pauseButton;

    Dialog dialog;

    Button plusButton;

    Spinner exercisesSpinner; //спиннер с упражнениями

    int currentExercise = 0; //счетчик упражнений


    private CountDownTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);



        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        statButton = (Button)findViewById(R.id.statButton);
        statButton.setOnClickListener(this);

        pauseButton = (Button)findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);

        tl = (TableLayout)findViewById(R.id.currentStatLayout);
        tableRow = (TableRow)findViewById(R.id.createdTableRow);
        textView = (TextView)findViewById(R.id.exercise1);
        textView1 = (TextView)findViewById(R.id.points1);
        button = (Button)findViewById(R.id.timeButton1);
        button.setOnClickListener(this);

        exerciseListTextView.add(0,textView);
        pointListTextView.add(0,textView1);
        timeListButton.add(0,button);

        plusButton = (Button)findViewById(R.id.plusButton);
        plusButton.setOnClickListener(this);

        exercisesSpinner = (Spinner)findViewById(R.id.exerciseSpinner);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,exercisesString);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exercisesSpinner.setAdapter(ad);
        exercisesSpinner.setPrompt("Упражнения");
        exercisesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exerciseListTextView.get(currentExercise).setText(exercisesString[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0; i<60; i++)
        {
            timeInt[i]=i;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.plusButton:
            {
                if (timeList.size()>currentExercise)
                {
                    TextView exTextView = new TextView(this);
                    exTextView.setLayoutParams(textView.getLayoutParams());
                    exTextView.setTextSize(30);
                    TextView poTextView = new TextView(this);
                    poTextView.setLayoutParams(textView1.getLayoutParams());
                    poTextView.setTextSize(30);
                    Button tButton = new Button(this);
                    tButton.setLayoutParams(button.getLayoutParams());
                    tButton.setTextSize(30);
                    tButton.setId(R.id.timeButton1);
                    tButton.setOnClickListener(this);
                    exTextView.setText(exercisesString[exercisesSpinner.getSelectedItemPosition()]);
                    poTextView.setText("0");
                    tButton.setText("00:00");
                    timeListButton.get(currentExercise).setEnabled(false); //black fucking magic
                    timeListButton.get(currentExercise).setId(0);
                    //adding previous to array
                    DataForDB dataForDB = new DataForDB(timeList.get(currentExercise).intValue(), Integer.valueOf(pointListTextView.get(currentExercise).getText().toString()).intValue(), exerciseListTextView.get(currentExercise).getText().toString());
                    dataForDBs.add(dataForDB);
                    currentExercise++;
                    exerciseListTextView.add(currentExercise, exTextView);
                    pointListTextView.add(currentExercise, poTextView);
                    timeListButton.add(currentExercise, tButton);
                    TableRow tr = new TableRow(this);
                    tr.addView(exTextView);
                    tr.addView(poTextView);
                    tr.addView(tButton);
                    tr.setLayoutParams(tableRow.getLayoutParams());
                    tl.addView(tr);
                    globalSeconds=0;

                }
                break;
            }
            case R.id.timeButton1:
            {
                showDialog(DIALOG);


                break;
            }


            case R.id.startButton:
            {
                if (timeList.size()>0)
                {
                    long time = timeList.get(currentExercise)*60;
                    if (globalSeconds!=0)
                    {
                        time-=globalSeconds;
                    }
                    showTimer(time*1000);
                    startButton.setEnabled(false);
                    plusButton.setEnabled(false);
                    run();
                }
                break;
            }

            case R.id.statButton:
            {
                Log.d(TAG,"start stat");
                Intent intent = new Intent(TableActivity.this, StatActivity.class);
                DBHelper dbHelper = new DBHelper(this,"FIXER_DB",null,1); //database
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                for (int i = 0; i<dataForDBs.size(); i++) //adding to DATABASE
                {
                    database.beginTransaction();
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put("time", dataForDBs.get(i).getTime());
                        cv.put("date", dataForDBs.get(i).getDate());
                        cv.put("points", dataForDBs.get(i).getCount());
                        cv.put("exercise", dataForDBs.get(i).getExercise());
                        long tmp = database.insert(TABLE_NAME, null, cv);
                        Log.d(TAG, Integer.valueOf((int) tmp).toString());
                        database.setTransactionSuccessful();
                    } finally {
                        database.endTransaction();
                    }
                }
                database.close();
                dataForDBs.clear();
                Log.d(TAG,"end stat");
                startActivity(intent);

            }
            case R.id.pauseButton:
            {
                if (timer!=null)
                {
                    timer.cancel();
                    timer=null;
                    timeList.add(currentExercise,(int)timeOfCurrentExercise);
                    startButton.setEnabled(true);
                    plusButton.setEnabled(true);
                }
                break;
            }



        }

    }

    private void run() {
        //todo : getting data from device
    }

    private void showTimer(long countDownMillis) {
        if(timer==null)
        {

            Log.d(TAG,"showTimer"+countDownMillis);
            timer = new CountDownTimer(countDownMillis,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long time = millisUntilFinished/1000;

                    if (time%60==0)
                    {
                        timeOfCurrentExercise=time/60;
                    } else
                    {
                        timeOfCurrentExercise=(time/60)+1;

                    }
                    globalSeconds=60-(int)time%60;
                    Log.d(TAG,Long.valueOf(time).toString());
                    int m = (int)time/60;
                    int s = (int)time%60;
                    String str="";
                    if (m<10)
                    {
                        str="0"+m;
                    }
                    else {
                        str=Integer.valueOf(m).toString();
                    }
                    str=str+":";
                    if (s<10)
                    {
                        str=str+"0"+s;
                    }
                    else
                    {
                        str=str+s;
                    }
                    final Button b = timeListButton.get(currentExercise);
                    final String text = str;
                    TableActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            b.setText(text);
                        }
                    });
                }

                @Override
                public void onFinish() {
                    final Button b = timeListButton.get(currentExercise);
                    TableActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            b.setText("00:00");
                        }
                    });
                    startButton.setEnabled(true);
                    plusButton.setEnabled(true);
                }
            }.start();
        }
    }



    @Override
    protected Dialog onCreateDialog (int id)
    {

        Log.d("logged","create");
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Выбор времени");
        ArrayAdapter<Integer> ad = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,timeInt);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adb.setSingleChoiceItems(ad,-1,myOnClickListener);
        adb.setPositiveButton("OK",myOnClickListener);
        adb.setNegativeButton("CANCEL",myOnClickListener);



        dialog = adb.create();

        return dialog;
    }

    DialogInterface.OnClickListener myOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {


            if (which==Dialog.BUTTON_POSITIVE)
            {
                ListView lv = ((AlertDialog) dialog).getListView();
                Button button = timeListButton.get(currentExercise);
                String tmp="";
                if (lv.getCheckedItemPosition()<10)
                {
                    tmp="0"+lv.getCheckedItemPosition();
                }
                else
                {
                    tmp=Integer.valueOf(lv.getCheckedItemPosition()).toString();
                }
                button.setText(tmp + ":00");
                timeList.add(currentExercise,lv.getCheckedItemPosition());
            }
        }


    };

    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table "+TABLE_NAME+" (id integer primary key autoincrement, "
                    + "time integer, "
                    + "date text, "
                    + "points integer, "
                    + "exercise text);");
            Log.d(TAG,"created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}