package com.example.FixerAppAndroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.PriorityQueue;

public class LoginActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    EditText loginEditText;
    EditText passwordEditText;
    Button goButton;
    ProgressBar progressBar;

    String login,password;

    SharedPreferences sharedPreferences;
    final String FIXER_LOGIN="fixerLogin";
    final String Fixer_PASS = "fixerPassword";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loginEditText = (EditText)findViewById(R.id.loginView);
        loginEditText.setOnClickListener(this);
        passwordEditText = (EditText)findViewById(R.id.passwordView);
        passwordEditText.setOnClickListener(this);
        goButton = (Button)findViewById(R.id.continueButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        //todo : add precheck

        //precheck (lol it doesn't work)

        if (!sharedPreferences.getString(FIXER_LOGIN,"").equals(""))
        {
            login=sharedPreferences.getString(FIXER_LOGIN,"");
            password=sharedPreferences.getString(Fixer_PASS,"");
            loginEditText.setVisibility(View.INVISIBLE);
            passwordEditText.setVisibility(View.INVISIBLE);
            goButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            onContinueButtonClick(goButton);
        }
    }


    public void onContinueButtonClick(View view) {
        //todo : add auth
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(FIXER_LOGIN,loginEditText.getText().toString());
        ed.putString(Fixer_PASS,passwordEditText.getText().toString());
        ed.commit();
        Intent intent = new Intent(LoginActivity.this, TableActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginView: {
                Log.d("click","login");
                if (loginEditText.getText().toString().equals("Login"))
                {
                    Log.d("check","login");
                    loginEditText.setText("");
                }
                break;
            }
            case R.id.passwordView: {
                Log.d("click","pw");
                if (passwordEditText.getText().toString().equals("Password"))
                {
                    Log.d("check","pw");
                    passwordEditText.setText("");
                }
            }
        }
    }
}
