package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondApp extends AppCompatActivity {
TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_app);
       score=(TextView) findViewById(R.id.score);

    }
    public void btnAdd1(View v){
      showScore(1);
    }
    public void btnAdd2(View v){
        showScore(2);
    }
    public void btnAdd3(View v){
        showScore(3);
    }
    public void btnReset(View v){
        score.setText("0");
    }
    private void showScore(int inc){
        Log.i("show","inc="+inc);
        String oldString =score.getText().toString();
       score.setText(Integer.parseInt(oldString)+inc);
    }
}
