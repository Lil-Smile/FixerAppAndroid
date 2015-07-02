package com.example.FixerAppAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.FixerAppAndroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smile on 29.06.15.
 */
public class TableActivity extends Activity implements View.OnClickListener {


    final String[] exercisesString = {"Рывок","Толчок","Д.цикл"};
    Integer[] timeInt = new Integer[60];
    int minutesInt = 0;
    int secondsInt = 0;



    List<TextView> exerciseListTextView = new ArrayList<TextView>(60); //для лэйблов с подходами
    List<TextView> pointListTextView = new ArrayList<TextView>(60); //очки
    List<Button> timeListButton = new ArrayList<Button>(60); //время


    TableLayout tl;
    TableRow tableRow; //строчка уже готовая и ее детки
    TextView textView;
    TextView textView1;
    Button button;
    Button timePickerButton;
    Button startButton;
    int timeButtonsId = R.id.timeButton1;

    Button plusButton;

    Spinner exercisesSpinner; //спиннер с упражнениями

    int currentExercise = 0; //счетчик упражнений

    RelativeLayout timePickerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        timePickerLayout = (RelativeLayout)findViewById(R.id.layoutWithTimePicker);
        timePickerButton = (Button)findViewById(R.id.timePickerButton);
        timePickerButton.setOnClickListener(this);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

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
                currentExercise++;
                exerciseListTextView.add(currentExercise,exTextView);
                pointListTextView.add(currentExercise,poTextView);
                timeListButton.add(currentExercise,tButton);
                TableRow tr = new TableRow(this);
                tr.addView(exTextView);
                tr.addView(poTextView);
                tr.addView(tButton);
                tr.setLayoutParams(tableRow.getLayoutParams());
                tl.addView(tr);
                break;
            }
            case R.id.timeButton1:
            {

                //RelativeLayout rl = (RelativeLayout)findViewById(R.id.tableMainLayout);
                //rl.setVisibility(View.INVISIBLE);
                final ArrayAdapter<Integer> minutes = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,timeInt);
                minutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spM = (Spinner)findViewById(R.id.spinnerMinutes);
                Spinner spS = (Spinner)findViewById(R.id.spinnerSeconds);
                spM.setPrompt("Минуты");
                spS.setPrompt("Секунды");
                spM.setAdapter(minutes);
                spS.setAdapter(minutes);
                spM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        minutesInt=position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        secondsInt=position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        secondsInt=0;
                    }
                });
                timePickerLayout.setVisibility(View.VISIBLE);

                break;
            }
            case R.id.timePickerButton:
            {

                //RelativeLayout rl = (RelativeLayout)findViewById(R.id.tableMainLayout);
                //rl.setVisibility(View.VISIBLE);
                timePickerLayout.setVisibility(View.INVISIBLE);
                timeListButton.get(currentExercise).setText(minutesInt+":"+secondsInt);
                break;
            }
            case R.id.startButton:
            {
                long time = minutesInt*60+secondsInt;
                //todo : реализовать нормальный таймер
                //run(time);
                break;
            }

        }

    }

    private void run(long time)
    {
        long timeStart = System.currentTimeMillis();
        long tmp = 0;
        Button bt = (Button)findViewById(R.id.timeButton1);
        while (true)
        {
            tmp = System.currentTimeMillis();
            tmp = tmp-timeStart;

            time=time-tmp/1000;
            bt.setText(time/60+":"+time%60);

            if (time<+0)
            {
                break;
            }

        }
    }

}