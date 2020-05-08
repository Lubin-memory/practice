package com.example.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondApp extends AppCompatActivity {
    public final String TAG="SecondActivity";
    TextView score;
    TextView score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//onCreat 设置页面布局
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_app);
       score=(TextView) findViewById(R.id.score);
        score2=(TextView) findViewById(R.id.score2);


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //处理屏幕旋转时数据丢失的问题  Bundle outState用来保留系统需要保留的数据
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.score2)).getText().toString();//转成字符串

        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  //通过这个方法去还原Bundle中的数据
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");//获取数据
        String scoreb = savedInstanceState.getString("teamb_score");

        Log.i(TAG, "onRestoreInstanceState: ");
        ((TextView)findViewById(R.id.score)).setText(scorea);//更新数据
        ((TextView)findViewById(R.id.score2)).setText(scoreb);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
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
TextView out =(TextView)findViewById(R.id.score);
        out.setText("0");
        ((TextView)findViewById(R.id.score2)).setText("0");
    }

    private void showScore(int inc){
        Log.i("show","inc="+inc);
        TextView out =(TextView)findViewById(R.id.score);
        String oldScore =out.getText().toString();
        String newScore=String.valueOf(Integer.parseInt(oldScore)+inc);
       out.setText(newScore);
    }

    private void showScore2(int inc){
        Log.i("show","inc="+inc);
        TextView out =(TextView)findViewById(R.id.score2);
        String oldScore =out.getText().toString();
        String newScore=String.valueOf(Integer.parseInt(oldScore)+inc);
        out.setText(newScore);
    }



}
