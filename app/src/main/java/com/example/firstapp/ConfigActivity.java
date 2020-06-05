package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {
    public final String TAG="ConfigActivity";
    EditText dollarText;
    EditText euroText;
    EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config); //test全是废话
       Intent intent =getIntent();
       float dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);//floa型数据的用法
        float euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        float won2=intent.getFloatExtra("won_rate_key",0.0f);
       Log.i(TAG,"onCreated: dollar2="+dollar2);
         Log.i(TAG,"onCreated: euro2="+euro2);
       Log.i(TAG,"onCreated: won2="+won2);
        dollarText=(EditText)findViewById(R.id.dollar_rate);
        euroText=(EditText)findViewById(R.id.euro_rate);
        wonText=(EditText)findViewById(R.id.won_rate);
        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }

public void save(View btn){
    Log.i(TAG,"save: ");
    //获取新的值
    float newDollar=Float.parseFloat(dollarText.getText().toString());
    float newEuro=Float.parseFloat(euroText.getText().toString());
    float newWon=Float.parseFloat(wonText.getText().toString());
    Log.i(TAG,"获取到新的值");
    Log.i(TAG,"save: newDollar="+newDollar);
    Log.i(TAG,"save: newEuro="+newEuro);
    Log.i(TAG,"save: newWon="+newWon);

    //保存到Bundle或放入到Extra中
    Intent intent=getIntent();
    Bundle bundle=new Bundle();
    bundle.putFloat("key_dollar",newDollar);
    bundle.putFloat("key_euro",newEuro);
    bundle.putFloat("key_won",newWon);
    intent.putExtras(bundle);
    setResult(2,intent);

    //返回到调用页面
    finish();//结束当前页面

}

}
