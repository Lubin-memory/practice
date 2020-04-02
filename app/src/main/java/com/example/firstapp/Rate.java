package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Rate extends AppCompatActivity {
EditText rmb;
TextView  show;
    float dollarRate=6.7f;
    float euroRate=(1/11.0f);
    float wonRate=500;
    String TAG="RateActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=(EditText) findViewById(R.id.rmb);
        show=(TextView) findViewById(R.id.showOut);
    }
    public void onClick(View btn){
        //获取用户输入
        float dollarRate=6.7f;
        float euroRate=(1/11.0f);
        float wonRate=500;
        String str=rmb.getText().toString();
        float r=0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else{//给用户提示信息
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();//消息提示  LENGTH_SHORT显示时间短，
        }

        if(btn.getId()==R.id.btn_dollar){
            // val=r * 6.7f;//加f完成强制转换
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.btn_euro){
          //   val=r * (1/11.0f);//加f完成强制转换
            show.setText(String.format("%.2f",r*euroRate));
        }else{
         //   val=r * 500;//加f完成强制转换
            show.setText(String.format("%.2f",r*wonRate));
        }

    }
    public void openOne(View v){
        Intent config=new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne:dollar_rate_key="+dollarRate);
        Log.i(TAG,"openOne:euro_rate_key="+euroRate);
        Log.i(TAG,"openOne:won_rate_key="+wonRate);
        startActivityForResult(config,1);//requestCode





        //打开一个页面activity
//        Log.i("open","openOne");
//        Intent hello=new Intent(this,SecondApp.class);//第一个参数是当前的上下文对象，即需要编辑下个窗口是由哪个窗口打开的，
//        //第二个参数是需要打开窗口的类名
//       // startActivity(hello);
//        //打开一个网页
//        Uri uri = Uri.parse("http://www.baidu.com");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//       // startActivity(intent);
//
//        //点击按钮转到拨号页面
//        Intent intent2 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"87092110"));
//        startActivity(intent2);

    }

}
