package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondApp extends AppCompatActivity {
    TextView score;
    TextView score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_app);
       score=(TextView) findViewById(R.id.score);
        score2=(TextView) findViewById(R.id.score2);

    }
    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn_1) {
            showScore(1);
        }
        else {
            showScore2(1);
        }
    }
    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn_2){
            showScore(2);}
        else{
            showScore2(2);}
    }
    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn_3){
            showScore(3);}
        else{
            showScore2(3);}
    }

    public void btnReset(View btn){

        score.setText("0");
    }

    private void showScore(int inc){
        Log.i("show","inc="+inc);
        String oldString =score.getText().toString();
       score.setText(Integer.parseInt(oldString)+inc);
    }
    private void showScore2(int inc){
        Log.i("show","inc="+inc);
        String oldString =score2.getText().toString();
        score2.setText(Integer.parseInt(oldString)+inc);
    }
}
